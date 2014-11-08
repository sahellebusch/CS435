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

int done, num_sims, darts_thrown, n_sim = 0;
pthread_mutex_t *random_lock;
pthread_mutex_t *sim_source_lock;

void *simulator(void *argc)
{
    double x = (double)rand() / (double)RAND_MAX;
    double y = (double)rand() / (double)RAND_MAX;
    if ( sqrt(x * x + y * y) <= 1)
    {
        printf("x = %f, y = %f.\n", x, y);
    }
    else
    {
        printf("miss!\n");
    }
    pthread_exit(NULL);
}

void *printer(void *argc)
{
    printf("hello, i'm the printer!\n");

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
    sscanf (argv[2], "%d", &num_sims);

    printf("num_sim_threads = %i, num_sims = %i\n", num_sim_threads, num_sims);

    random_lock = (pthread_mutex_t *) malloc (sizeof (pthread_mutex_t));
    pthread_mutex_init(random_lock, NULL);
    sim_source_lock = (pthread_mutex_t *) malloc (sizeof (pthread_mutex_t));
    pthread_mutex_init(sim_source_lock, NULL);

    // Create printer(consumer) threads
    pthread_t *printer_thread = (pthread_t *) malloc (sizeof(pthread_t));

    if (pthread_create (printer_thread, NULL, printer, NULL))
    {
        fprintf(stderr, "Error creating simulator thread %i.\n", i);
        exit(-1);
    }

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