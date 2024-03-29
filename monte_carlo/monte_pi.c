/* Author         =>  Sean Hellebusch
 * Revision       =>  1
 * Date           =>  2014-11-09
 *
 * This program simulates the Monte Carlo
 * method to find the value of pi.
 *
 */

#include <pthread.h>
#include <stdlib.h>
#include <stdio.h>

#define MILLION 1000000

int done      = 0;
int can_print = 0;
int num_sims, darts_thrown, n_sim, hits;
// mutex to protect the random number generator
pthread_mutex_t *random_lock;
// mutex to protect shared variables
pthread_mutex_t *var_lock;
pthread_cond_t  *print_ready;

/*
 * Function: simulator
 *
 * Simulates throwing a dart as per the Monte Carlo
 * method of finding area in a region.  Alerts the
 * printer when it can print.
 *
 */
void *simulator()
{
  while (!done)
  {
    pthread_mutex_lock(random_lock);

    double x = (double)rand() / (double)RAND_MAX;
    double y = (double)rand() / (double)RAND_MAX;

    pthread_mutex_unlock(random_lock);

    pthread_mutex_lock(var_lock);

    darts_thrown++;

    if((x * x + y * y) <= 1)
      hits++;

    /* if we have a multiple of a million, change condition var to
     * satisfy printer's predicate and signal. This will termiante the
     * printer if given a multiple of a million # of simulations
     */
    if((darts_thrown > 0) && (darts_thrown % MILLION == 0))
    {
      can_print = ~can_print;
      pthread_cond_signal(print_ready);
    }

    /* If we have a non-multiple of a million simulations, explicitly
     * tell the printer to print one more time and join.
     */
    if(darts_thrown == num_sims)
    {
      can_print = 1;
      done = ~done;
      pthread_cond_signal(print_ready);
    }

    pthread_mutex_unlock(var_lock);

  }

  pthread_exit(NULL);
}

/*
 * Function : printer
 *
 * When awoken, will print out the current
 * estimation of pi.
 *
 */
void *printer()
{
  double curr_estimate;

  while(!done)
  {

    pthread_mutex_lock(var_lock);

    // while we cannot print, wait and go to sleep
    while(!can_print)
      pthread_cond_wait(print_ready, var_lock);

    // calculate current estimate of pi
    curr_estimate = (((double)hits) / darts_thrown) * 4;
    can_print = ~can_print;

    pthread_mutex_unlock(var_lock);
  
    printf("The current estimation of pi is: %f\n", curr_estimate);
  }


  pthread_exit(NULL);
}

int main(int argc, char *argv[])
{
  int num_sim_threads, i;
  srand((unsigned) time(NULL));

  if(argc != 3)
  {
    printf("Error: Incorrect number of arguments.\n" );
    exit(-1);
  }

  sscanf (argv[1], "%d", &num_sim_threads);
  sscanf (argv[2], "%i", &num_sims);

  /*
   * Our program is required to print the current estimate of pi every
   * 1 million simulations.  Don't waste CPU resources if the user enters
   * less than a million simulations.  Just exit.
   */
  if(num_sims < MILLION)
  {
    printf("You have entered less than one million simulations, no estimate will be displayed.\n");
    exit(-1);
  }
  // malloc the locks
  random_lock = (pthread_mutex_t *) malloc (sizeof (pthread_mutex_t));
  pthread_mutex_init(random_lock, NULL);
  var_lock    = (pthread_mutex_t *) malloc (sizeof (pthread_mutex_t));
  pthread_mutex_init(var_lock, NULL);

  // malloc the condition variable
  print_ready = (pthread_cond_t *) malloc (sizeof(pthread_cond_t));
  pthread_cond_init(print_ready, NULL);

  // Create printer(consumer) thread
  pthread_t *printer_thread = (pthread_t *) malloc (sizeof(pthread_t));

  if(pthread_create (printer_thread, NULL, printer, NULL))
  {
    fprintf(stderr, "Error creating simulator thread %i.\n", i);
    exit(-1);
  }

  // Create simulation(producer) threads
  pthread_t **simulation_thread = (pthread_t **) malloc (sizeof(pthread_t *) * num_sim_threads);
  for(i = 0; i < num_sim_threads; i++)
  {
    simulation_thread[i] = (pthread_t *) malloc (sizeof(pthread_t));

    if(pthread_create (simulation_thread[i], NULL, simulator, NULL))
    {
      fprintf(stderr, "Error creating simulator thread %i.\n", i);
      exit(-1);
    }
  }

  // join simulation threads
  for(i = 0; i < num_sim_threads; i++)
  {
    if(pthread_join(*simulation_thread[i], NULL))
    {
      fprintf(stderr, "Error joining simulator thread %i.\n", i);
      exit(-1);
    }
  }

  // join printer thread
  if(pthread_join(*printer_thread, NULL))
  {
    fprintf(stderr, "Error joining simulator thread %i.\n", i);
    exit(-1);
  }

  //clean up and exit cleanly
  pthread_cond_destroy(print_ready);
  pthread_mutex_destroy(var_lock);
  pthread_mutex_destroy(random_lock);
  exit(0);
}
