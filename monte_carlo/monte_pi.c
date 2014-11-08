/* Author         =>  Sean Hellebusch
 * Revision       =>  1
 * Date           =>  2014-10-09
 *
 * This program determines common meetings times among
 * three people using concurrency.  The times are
 * predefined in a text file.
 */

#include <pthread.h>
#include <stdlib.h>
#include <stdio.h>
#include <time.h>
#include <math.h>

#define MILLION 1000000

int num_sims, darts_thrown, n_sim, hits = 0;
int done = 1;
int can_print = -1;
pthread_mutex_t *random_lock;
pthread_mutex_t *sim_source_lock;
pthread_mutex_t *print_lock;
pthread_cond_t  *print_ready;

void *simulator(void *argc)
{
    printf("created simulator\n");
    while (!done)
    {
        pthread_mutex_lock(random_lock);
        printf("i've got the random lock");
        double x = (double)rand() / (double)RAND_MAX;
        double y = (double)rand() / (double)RAND_MAX;
        printf("I'm giving up the random lock");
        pthread_mutex_unlock(random_lock);

        pthread_mutex_lock(sim_source_lock);
        darts_thrown++;
        printf("darts thrown = %i\n", darts_thrown );
        if (darts_thrown == num_sims)
        {
            done = -1;
        }
        if (darts_thrown % MILLION == 0)
        {
            can_print = -1;
        }
        if (sqrt(x * x + y * y) <= 1)
        {
            hits++;
        }
        pthread_mutex_unlock(sim_source_lock);
        if (can_print)
        {
            printf("can print, throws = %i", num_sims);
            pthread_cond_broadcast(print_ready);
        }
    }
    pthread_exit(NULL);
}

void *printer(void *argc)
{
    printf("created printer\n");
    double curr_area = 0;
    while (!done)
    {

        pthread_mutex_lock(print_lock);
        printf("print = %i.\n", can_print);
        while (!can_print)
        {
            printf("here\n");
            pthread_cond_wait(print_ready, print_lock);
            printf("here\n");
        }

        curr_area = hits / num_sims;
        printf("Simulation number: %i.  Current calculated area: %f", num_sims, curr_area);
    }
    pthread_exit(NULL);
}

int main(int argc, char *argv[])
{
    int num_sim_threads, i = 0;
    srand((unsigned) time(NULL));
    if (argc != 3)
    {
        printf( "Error: Incorrect number of arguments.\n" );
        exit(-1);
    }

    sscanf (argv[1], "%d", &num_sim_threads);
    sscanf (argv[2], "%i", &num_sims);

    printf("num_sim_threads = %i, num_sims = %i\n", num_sim_threads, num_sims);

    random_lock = (pthread_mutex_t *) malloc (sizeof (pthread_mutex_t));
    pthread_mutex_init(random_lock, NULL);
    sim_source_lock = (pthread_mutex_t *) malloc (sizeof (pthread_mutex_t));
    pthread_mutex_init(sim_source_lock, NULL);
    print_lock = (pthread_mutex_t *) malloc (sizeof (pthread_mutex_t));

    // Create printer(consumer) threads
    pthread_t *printer_thread = (pthread_t *) malloc (sizeof(pthread_t));

    if (pthread_create (printer_thread, NULL, printer, NULL))
    {
        fprintf(stderr, "Error creating simulator thread %i.\n", i);
        exit(-1);
    }

    // Create simulation(producer) threads
    pthread_t **simulation_thread = (pthread_t **) malloc (sizeof(pthread_t *) * num_sim_threads);
    for (i = 0; i < num_sim_threads; i++)
    {
        simulation_thread[i] = (pthread_t *) malloc (sizeof(pthread_t));

        if (pthread_create (simulation_thread[i], NULL, simulator, NULL))
        {
            fprintf(stderr, "Error creating simulator thread %i.\n", i);
            exit(-1);
        }
    }

    for ( i = 0; i < num_sim_threads; i++)
    {
        if (pthread_join(*simulation_thread[i], NULL))
        {
            fprintf(stderr, "Error joining simulator thread %i.\n", i);
            exit(-1);
        }
    }

    if (pthread_join(*printer_thread, NULL))
    {
        fprintf(stderr, "Error joining simulator thread %i.\n", i);
        exit(-1);
    }
    exit(0);
}