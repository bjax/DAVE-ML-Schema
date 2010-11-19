function interp_discrete_examples()
% Script to generate discrete interpolation examples for DAVE-ML
% reference guide.
%
% 2008-10-09 Bruce Jackson <bruce.jackson@nasa.gov>
%

fontsize = 12;

iv_points = [1 3 4 6 7.5];
dv_points = [2 6 5 7 1.5];
iv = [-1 iv_points 10];
mid = diff(iv_points)/2 + iv_points(1:length(iv_points)-1);
iv_mid   = sort([-1 mid mid 10]);
iv_ceil  = [-1 iv_points([1 1 2 2 3 3 4 4]) 10];
iv_floor = [-1 iv_points([2 2 3 3 4 4 5 5]) 10];
dv_doubled = dv_points([1 1 2 2 3 3 4 4 5 5]);


% 1: interpolate DISCRETE
subplot(3,1,1)
plot_points(iv_points, dv_points, fontsize);
hold on;
plot(iv_mid,dv_doubled,'-');
hold off;
text(1,7,'interpolate="discrete"','FontSize',fontsize)


% 2: interpolate CEILING
subplot(3,1,2)
plot_points(iv_points, dv_points, fontsize);
hold on;
plot(iv_ceil,dv_doubled,'-');
hold off;
text(1,7,'interpolate="ceiling"','FontSize',fontsize)


% 3: interpolate FLOOR
subplot(3,1,3)
plot_points(iv_points, dv_points, fontsize);
hold on;
plot(iv_floor,dv_doubled,'-');
hold off;
text(1,7,'interpolate="floor"','FontSize',fontsize)


