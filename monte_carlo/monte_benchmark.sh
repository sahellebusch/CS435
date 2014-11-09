#!/bin/bash

mapfile -t test11 < <(./monte_pi 1 1000000)
mapfile -t test12 < <(./monte_pi 1 10000000)
mapfile -t test13 < <(./monte_pi 1 100000000)


mapfile -t test21 < <(./monte_pi 2 1000000)
mapfile -t test22 < <(./monte_pi 2 10000000)
mapfile -t test23 < <(./monte_pi 2 100000000)


mapfile -t test31 < <(./monte_pi 4 1000000)
mapfile -t test32 < <(./monte_pi 4 10000000)
mapfile -t test33 < <(./monte_pi 4 100000000)

printf "Threads     Simulations      Time\(s\)\n"
printf "1             100000         ${test11[1]}\n"
printf "1             1000000        ${test12[10]}\n"
printf "1             1000000        ${test13[100]}\n"
printf "2             100000         ${test21[1]}\n"
printf "2             1000000        ${test22[10]}\n"
printf "2             1000000        ${test23[100]}\n"
printf "4             100000         ${test31[1]}\n"
printf "4             1000000        ${test32[10]}\n"
printf "4             1000000        ${test33[100]}\n"


