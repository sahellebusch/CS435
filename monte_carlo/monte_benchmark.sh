#!/bin/bash

echo
mapfile -t CPU < <(grep "model name" /proc/cpuinfo)
echo $CPU[0]
mapfile -t PROC < <(grep -c ^processor /proc/cpuinfo)
echo "total processors: ${PROC}"

test11=$({ time ./monte_pi 1 1000000 >/dev/null; } |& grep real )
test12=$({ time ./monte_pi 1 10000000 >/dev/null; } |& grep real )
test13=$({ time ./monte_pi 1 1000000000 >/dev/null; } |& grep real )


test21=$({ time ./monte_pi 1 1000000 >/dev/null; } |& grep real )
test22=$({ time ./monte_pi 1 10000000 >/dev/null; } |& grep real )
test23=$({ time ./monte_pi 1 1000000000 >/dev/null; } |& grep real )

test31=$({ time ./monte_pi 1 1000000 >/dev/null; } |& grep real )
test32=$({ time ./monte_pi 1 10000000 >/dev/null; } |& grep real )
test33=$({ time ./monte_pi 1 1000000000 >/dev/null; } |& grep real )

# For really big server spaces, use this to find the 
# system's number of processors and run it.
# test41=$({ time ./monte_pi 1 1000000 >/dev/null; } |& grep real )
# test42=$({ time ./monte_pi 1 10000000 >/dev/null; } |& grep real )
# test43=$({ time ./monte_pi 1 1000000000 >/dev/null; } |& grep real )

printf "Threads     Simulations            Time(s)\n"           
printf "1             1,000,000        $test11\n"
printf "1             10,000,000       $test12\n" 
printf "1             1,000,000,000    $test13\n"
echo "-----------------------------------------------------------"
printf "2             1,000,000        $test21\n"
printf "2             10,000,000       $test22\n" 
printf "2             1,000,000,000    $test23\n"
echo "-----------------------------------------------------------"
printf "4             1,000,000        $test31\n"
printf "4             10,000,000       $test32\n" 
printf "4             1,000,000,000    $test33\n"
echo "-----------------------------------------------------------"
printf "${PROC}             1,000,000        $test41\n"
printf "${PROC}             10,000,000       $test42\n"
printf "${PROC}             1,000,000,000    $test43\n"


