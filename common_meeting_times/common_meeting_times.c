/* Author         =>  Sean Hellebusch
 * Revision       =>  1
 * Date           =>  2014-10-09
 *
 * This program determines common meetings times among
 * three people using concurrency.  The times are
 * predefined in a text file.
 */

#include <stdio.h>
#include <stdlib.h>

int *person2, *person3;
int size_person2, size_person3;

/*
 * Function: searcher
 * param: *arg - the time we are searching for
 *
 * The searcher will determine if a meeting time is common
 * among the three people.  If it is, it will print the
 * common time to the terminal, otherwise return NULL.
 */
void *searcher ( void *arg )
{
    int i;
    int number = *( int *) arg;
    free(arg);

    for ( i = 0; i < size_person2; i++ )
    {
        if ( number == person2[i] )
        {
            for ( i = 0; i < size_person3; i++ )
            {
                if ( number == person3[i] )
                {
                    printf( "%i is a common meeting time.\n", number );
                    return ( NULL );
                }
            }
        }
    }
    return ( NULL );
}


int main( int argc, char **argv)
{

    int *person1, *value_to_find;
    int num_times_avail, num_threads, i;

    if ( argc != 2 )
    {
        printf( "Error: Incorrect number of arguments.\n" );
    }
    else
    {
        FILE *file = fopen( argv[1], "r");

        if ( file == NULL )
        {
            printf("Could not open file \"%s\".\n", argv[1] );
            exit( -1 );
        }

        // store size of first line as number of threads to spawn
        fscanf( file, "%i", &num_times_avail );
        num_threads = num_times_avail;

        // store each following int in line 1 in person1[]
        person1 = ( int *) malloc( num_times_avail * sizeof( int ));
        for ( i = 0; i < num_times_avail; i++ )
        {
            fscanf( file, "%i", &person1[i] );
        }

        // for person 2 & 3, store length and following meeting times
        // into respective size and array
        fscanf( file, "%i", &num_times_avail );
        person2 = ( int *)malloc( ( num_times_avail ) * sizeof( int ));
        size_person2 = num_times_avail;
        for ( i = 0; i < num_times_avail; i++ )
        {
            fscanf( file, "%i", &person2[i] );
        }

        fscanf( file, "%i", &num_times_avail );
        person3 = ( int *)malloc( ( num_times_avail ) * sizeof( int ));
        size_person3 = num_times_avail;
        for ( i = 0; i < num_times_avail; i++ )
        {
            fscanf( file, "%i", &person3[i] );
        }

        // done reading from file, close it
        fclose(file);

        // declare pthread_t array ptr
        pthread_t **tid = ( pthread_t **)
                          malloc( sizeof( pthread_t *) *num_threads );
        // allocate memory for pthreads
        for ( i = 0; i < num_threads; i++ )
        {
            tid[i] = ( pthread_t *) malloc ( sizeof( pthread_t ));
        }

        // now create threads and search!
        for ( i = 0; i < num_threads; i++)
        {
            value_to_find = malloc( sizeof (int) );
            *value_to_find = person1[i] ;
            if ( pthread_create ( tid[i], NULL,
                                  searcher, ( void *) value_to_find ))
            {
                fprintf( stderr, "Error creating thread %d.\n", i );
                exit( -1 );
            }
        }

        for ( i = 0; i < num_threads; i++ )
        {
            if ( pthread_join( *tid[i], NULL ))
            {
                fprintf( stderr, "Error joining thread %i.\n", i );
                exit( -1 );
            }
        }
        // free ptr memory
        free(person1);
        free(person2);
        free(person3);
    }
    return 0;
}
