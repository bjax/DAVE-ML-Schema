function plot_points(iv_points, dv_points, fs)
% Utility routine for interp example scripts of DAVE-ML ref guide
% 2008-10-09 Bruce Jackson <bruce.jackson@nasa.gov>
%
theAxis = [0 9 0 9];
plot(iv_points,dv_points,'o');
axis([0 9 0 9]);
xlabel('Independent variable','FontSize',fs);
ylabel('Dependent variable','FontSize',fs);
