/* Author         =>  Sean Hellebusch
 * Revision       =>  1
 * Date           =>  2014-11-08
 *
 * This program simulates the Monte Carlo
 * method to find the value of pi.
 */

#include <pthread.h>
#include <stdlib.h>
#include <stdio.h>
#include <time.h>
#include <math.h>
#include <time.h>

#define MILLION 1000000

int num_sims, darts_thrown, n_sim, hits;
int done      = -1;
int can_print = -1;
pthread_mutex_t *random_lock;
pthread_mutex_t *sim_source_lock;
pthread_mutex_t *print_lock;
pthread_cond_t  *print_ready;

void *simulator(void *argc)
{
  while (done < 0)
  {
    pthread_mutex_lock(random_lock);
    double x = (double)rand() / (double)RAND_MAX;
    double y = (double)rand() / (double)RAND_MAX;
    pthread_mutex_unlock(random_lock);

    pthread_mutex_lock(sim_source_lock);
    darts_thrown++;
    if(darts_thrown == num_sims)
      done = 0;
    if((darts_thrown > 0) && (darts_thrown % MILLION == 0))
      can_print = 0;
    if(sqrt(x*x  + y*y) <= 1)
      hits++;
    if(can_print == 0)
      pthread_cond_broadcast(print_ready);
    pthread_mutex_unlock(sim_source_lock);

  }
  pthread_exit(NULL);
}

void *printer(void *argc)
{
  double curr_estimate;
  while(done < 0)
  {
    pthread_mutex_lock(print_lock);
    // while we cannot print, wait and go to sleep
    while(can_print < 0 )
      pthread_cond_wait(print_ready, print_lock);

    curr_estimate = (((double)hits) / darts_thrown) * 4;
    printf("Pi: %f\n", curr_estimate);
    can_print = -1;
    pthread_mutex_unlock(print_lock);
  }
  pthread_exit(NULL);
}

int main(int argc, char *argv[])
{
  clock_t begin, end;
  double exec_time;
  int num_sim_threads, i = 0;
  srand((unsigned) time(NULL));
  if(argc != 3)
  {
    printf( "Error: Incorrect number of arguments.\n" );
    exit(-1);
  }

  sscanf (argv[1], "%d", &num_sim_threads);
  sscanf (argv[2], "%i", &num_sims);

  random_lock = (pthread_mutex_t *) malloc (sizeof (pthread_mutex_t));
  pthread_mutex_init(random_lock, NULL);
  sim_source_lock = (pthread_mutex_t *) malloc (sizeof (pthread_mutex_t));
  pthread_mutex_init(sim_source_lock, NULL);
  print_lock = (pthread_mutex_t *) malloc (sizeof (pthread_mutex_t));

  print_ready = (pthread_cond_t *) malloc (sizeof(pthread_cond_t));
  pthread_cond_init(print_ready, NULL);

  begin = clock();
  // Create printer(consumer) threads
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
  end = clock();

  exec_time = (double)(end - begin) / CLOCKS_PER_SEC;
  printf("%f\n", exec_time);
  exit(0);
}