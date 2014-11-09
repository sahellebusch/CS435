#!/bin/bash

echo
mapfile -t CPU < <(grep "model name" /proc/cpuinfo)
echo $CPU[0]
mapfile -t PROC < <(grep -c ^processor /proc/cpuinfo)
echo "total processors: ${PROC}"
PI=3.141592
echo "real value of pi: $PI"
echo 

mapfile -t test11 < <(./monte_pi 1 1000000)
mapfile -t test12 < <(./monte_pi 1 10000000)
# mapfile -t test13 < <(./monte_pi 1 1000000000)

# mapfile -t test21 < <(./monte_pi 2 1000000)
# mapfile -t test22 < <(./monte_pi 2 10000000)
# mapfile -t test23 < <(./monte_pi 2 1000000000)


# mapfile -t test31 < <(./monte_pi 4 1000000)
# mapfile -t test32 < <(./monte_pi 4 10000000)
# mapfile -t test33 < <(./monte_pi 4 1000000000)

# mapfile -t test41 < <(./monte_pi ${PROC} 1000000)
# mapfile -t test42 < <(./monte_pi ${PROC} 10000000)
# mapfile -t test43 < <(./monte_pi ${PROC} 1000000000)

printf "Threads     Simulations      Time(s)     Estd. Pi   Percent Error \n"            
printf "1             1,000,000        ${test11[1]}  ${test11[0]#*:}   $(echo "scale=7;((${test11[0]#*:} - $PI) / $PI) * 100" | bc -l)\n"
printf "1             10,000,000       ${test12[10]}  ${test12[0]#*:}   $(echo "scale=7;((${test12[0]#*:} - $PI) / $PI) * 100" | bc -l)\n"
printf "1             1,000,000,000    ${test13[1000]}  ${test13[0]#*:}  $(echo "scale=7;((${test13[0]#*:} - $PI) / $PI) * 100" | bc -l)\n"
echo "-----------------------------------------------------------"
printf "2             1,000,000        ${test21[1]}  ${test21[0]#*:}   $(echo "scale=7;((${test21[0]#*:} - $PI) / $PI) * 100" | bc -l)\n"
printf "2             10,000,000       ${test22[10]}  ${test22[0]#*:}   $(echo "scale=7;((${test22[0]#*:} - $PI) / $PI) * 100" | bc -l)\n"
printf "2             1,000,000,000    ${test23[1000]}  ${test23[0]#*:}   $(echo "scale=7;((${test23[0]#*:} - $PI) / $PI) * 100" | bc -l)\n"
echo "-----------------------------------------------------------"
printf "4             1,000,000        ${test31[1]}  ${test31[0]#*:}   $(echo "scale=7;((${test31[0]#*:} - $PI) / $PI) * 100" | bc -l)\n"
printf "4             10,000,000       ${test32[10]}  ${test32[0]#*:}   $(echo "scale=7;((${test32[0]#*:} - $PI) / $PI) * 100" | bc -l)\n"
printf "4             1,000,000,000    ${test33[1000]}  ${test33[0]#*:}   $(echo "scale=7;((${test33[0]#*:} - $PI) / $PI) * 100" | bc -l)\n"
echo "-----------------------------------------------------------"
printf "${PROC}             1,000,000        ${test41[1]}  ${test41[0]#*:}   $(echo "scale=7;((${test41[0]#*:} - $PI) / $PI) * 100" | bc -l)\n"
printf "${PROC}             10,000,000       ${test42[10]}  ${test42[0]#*:}   $(echo "scale=7;((${test42[0]#*:} - $PI) / $PI) * 100" | bc -l)\n"
printf "${PROC}             1,000,000,000    ${test43[1000]} ${test43[0]#*:}    $(echo "scale=7;((${test43[0]#*:} - $PI) / $PI) * 100" | bc -l)\n"


