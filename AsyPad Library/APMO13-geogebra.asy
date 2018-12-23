 /* Geogebra to Asymptote conversion, documentation at artofproblemsolving.com/Wiki go to User:Azjps/geogebra */
import graph; size(15.78cm); 
real labelscalefactor = 0.5; /* changes label-to-point distance */
pen dps = linewidth(0.7) + fontsize(10); defaultpen(dps); /* default pen style */ 
pen dotstyle = black; /* point style */ 
real xmin = -7.89, xmax = 7.89, ymin = -5., ymax = 5.;  /* image dimensions */

pair A = (-5.71,3.96), B = (-6.03,-0.6), D = (-0.43,-0.2), P = (-2.9359259259259267,-4.5170370370370385), C = (-3.804753577106519,-1.862066772655008), Q = (-1.5642833193629506,-2.1540653813914505), R = (2.766558082360173,-2.7185003073140757); 
 /* draw figures */
draw(circle((-3.3660201511335015,1.5042821158690174), 3.394818383703753), linewidth(2.)); 
draw((xmin, -1.2659803686856597*xmin-8.233861623174528)--(xmax, -1.2659803686856597*xmax-8.233861623174528), linewidth(2.)); /* line */
draw((xmin, 1.7227313035767073*xmin + 0.5407744605379845)--(xmax, 1.7227313035767073*xmax + 0.5407744605379845), linewidth(2.)); /* line */
draw((xmin, -3.0558077436582125*xmin-13.488662216288393)--(xmax, -3.0558077436582125*xmax-13.488662216288393), linewidth(2.)); /* line */
draw((xmin, -0.13032916091040697*xmin-2.35793711383017)--(xmax, -0.13032916091040697*xmax-2.35793711383017), linewidth(2.)); /* line */
draw(A--Q, linewidth(2.)); 
draw((xmin, -0.7878787878787878*xmin-0.5387878787878788)--(xmax, -0.7878787878787878*xmax-0.5387878787878788), linewidth(2.)); /* line */
draw((xmin, -0.24083286752375632*xmin-2.0522221911682506)--(xmax, -0.24083286752375632*xmax-2.0522221911682506), linewidth(2.)); /* line */
 /* dots and labels */
dot(A,dotstyle); 
label("$A$", (-5.63,4.16), NE * labelscalefactor); 
dot(B,dotstyle); 
label("$B$", (-5.95,-0.4), NE * labelscalefactor); 
dot(D,dotstyle); 
label("$D$", (-0.35,0.), NE * labelscalefactor); 
dot(P,linewidth(4.pt) + dotstyle); 
label("$P$", (-2.85,-4.36), NE * labelscalefactor); 
dot(C,linewidth(4.pt) + dotstyle); 
label("$C$", (-3.73,-1.7), NE * labelscalefactor); 
dot(Q,linewidth(4.pt) + dotstyle); 
label("$Q$", (-1.49,-2.), NE * labelscalefactor); 
dot((-1.952119859435844,-1.5820875676702444),linewidth(4.pt) + dotstyle); 
label("$E$", (-1.87,-1.42), NE * labelscalefactor); 
dot(R,linewidth(4.pt) + dotstyle); 
label("$R$", (2.85,-2.56), NE * labelscalefactor); 
clip((xmin,ymin)--(xmin,ymax)--(xmax,ymax)--(xmax,ymin)--cycle); 
 /* end of picture */