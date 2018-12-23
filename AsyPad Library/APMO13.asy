//Generated By AsyPadv1.0
import olympiad;
import markers;
import math;
import graph;
//change the unit size to fit your needs
unitsize(1cm);
//dependency level 0
/* You can change the coordinates of these points of dependency level 0.
The drawing will retain the same relationships and qualities.
Please be aware that as a result of this some of the image may be clipped off. */
pair O = (3.91, 17.09); dot(O); label("$O$", O, SE);
pair A = (5.11, 17.34); dot(A); label("$A$", A, SE);
//dependency level 1
//Do not change anything below, unless you are experienced in Asymptote.
path circOA = Circle(O, abs(O-A)); draw(circOA);
//dependency level 2
pair B = relpoint(circOA, -0.7215088337730656); dot(B); label("$B$", B, SE);
pair D = relpoint(circOA, -0.15943168564932905); dot(D); label("$D$", D, SE);
//dependency level 3
path segOB = O--B; 
path segOD = O--D; 
path lineAD = (A-20.0*unit(D-A))--(D+20.0*unit(D-A)); draw(lineAD);
//dependency level 4
path perBsegOB = (B-20.0*(dir(segOB).y, -dir(segOB).x))--(B+20.0*(dir(segOB).y, -dir(segOB).x)); draw(perBsegOB);
path perDsegOD = (D-20.0*(dir(segOD).y, -dir(segOD).x))--(D+20.0*(dir(segOD).y, -dir(segOD).x)); draw(perDsegOD);
//dependency level 5
pair P = intersectionpoint(perBsegOB, perDsegOD); dot(P); label("$P$", P, SE);
//dependency level 6
path linePA = (P-20.0*unit(A-P))--(A+20.0*unit(A-P)); 
//dependency level 7
pair C = intersectionpoints(linePA, circOA)[1]; dot(C); label("$C$", C, SE);
//dependency level 8
path segOC = O--C; 
//dependency level 9
path perCsegOC = (C-20.0*(dir(segOC).y, -dir(segOC).x))--(C+20.0*(dir(segOC).y, -dir(segOC).x)); draw(perCsegOC);
//dependency level 10
pair Q = intersectionpoint(perDsegOD, perCsegOC); dot(Q); label("$Q$", Q, SE);
pair R = intersectionpoint(perCsegOC, lineAD); dot(R); label("$R$", R, SE);
//dependency level 11
path segAQ = A--Q; draw(segAQ);
path segBR = B--R; draw(segBR);
//dependency level 12
pair E = intersectionpoints(segAQ, circOA)[1]; dot(E); label("$E$", E, SE);
//clip the drawing view
clip((0.0, 13.19)--(0.0, 20.0)--(10.0, 20.0)--(10.0, 13.19)--cycle);