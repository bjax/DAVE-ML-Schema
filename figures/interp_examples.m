function interp_example();
% Script to generate interpolation/extrapolation examples for DAVE-ML
% reference guide.
%
% 2008-10-09 Bruce Jackson <bruce.jackson@nasa.gov>
%

fontsize = 12;  % font size
labelx = 3;
label1y = 2;
label2y = 1.3;

iv = [1 3 4 6 7.5];
dv = [2 6 5 7 1.5];
n = length(dv);

ivx = [-1 iv 10];
mid = diff(iv)/2 + iv(1:n-1);
iv_mid   = sort([-1 mid mid 10]);
iv_ceil  = [-1 iv([1 1 2 2 3 3 4 4]) 10];
iv_floor = [-1 iv([2 2 3 3 4 4 5 5]) 10];
dv_doubled = dv([1 1 2 2 3 3 4 4 5 5]);

theAxis = [0 9 0 9];
subplot(1,1,1)

% 1: extrapolate NONE, interpolate LINEAR
subplot(3,2,1)
dv1 = dv([1 1 2 3 4 5 5]);
plot_points(iv, dv, fontsize);
hold on;
plot(ivx,dv1,'-');
hold off;
text(labelx,label1y,'extrapolate="none"','FontSize',fontsize);
text(labelx,label2y,'interpolate="linear"','FontSize',fontsize)


% 2: extrapolate BOTH, interpolate LINEAR
subplot(3,2,2)
plot_points(iv, dv, fontsize);
dv2 = dv1;
slope1 = (dv(2)-dv(1))/(iv(2)-iv(1));
dv2(1) = dv(1) + slope1*(ivx(1)-ivx(2));
slope2 = (dv(5)-dv(4))/(iv(5)-iv(4));
dv2(7) = dv(5) + slope2*(ivx(7)-ivx(6));
hold on;
plot(ivx,dv2,'-');
hold off;
text(labelx,label1y,'extrapolate="both"','FontSize',fontsize);
text(labelx,label2y,'interpolate="linear"','FontSize',fontsize)


% 3: extrapolate NONE, interpolate QUADRATIC
subplot(3,2,3)
plot_points(iv, dv, fontsize);
hold on;
x=min(iv):0.2:max(iv);
x=[x max(iv)];
y=quadratic(1.29167,x,iv,dv);% near-optimal initial slope
x=[-1 x 10];
y=[dv(1) y' dv(n)];
plot(x,y,'-');hold off;
text(labelx,label1y,'extrapolate="none"','FontSize',fontsize);
text(labelx,label2y,'interpolate="quadratic"','FontSize',fontsize)


% 4: extrapolate BOTH, interpolate QUADRATIC
subplot(3,2,4)
plot_points(iv, dv, fontsize);
hold on;
x=min(ivx):0.2:max(ivx);
y=quadratic(1.29167,x,iv,dv);% near-optimal initial slope
plot(x,y,'-');
hold off;
text(labelx,label1y,'extrapolate="both"','FontSize',fontsize);
text(labelx,label2y,'interpolate="quadratic"','FontSize',fontsize)


% 5: extrapolate NONE, interpolate CUBIC
subplot(3,2,5)
plot_points(iv, dv, fontsize);
hold on;
x=min(iv):0.2:max(iv);
y=interp1(iv,dv,x,'cubic');
x=[min(ivx) x iv(n) max(ivx)];
y=[dv(1) y dv(n) dv(n)];
plot(x,y,'-');
% extrapolation slope lines either end
plot(iv([1 2]),dv([1 2]),'g-.')
plot(iv([n-1 n]),dv([n-1 n]),'g-.')
hold off;
text(labelx,label1y,'extrapolate="none"','FontSize',fontsize);
text(labelx,label2y,'interpolate="cubic"','FontSize',fontsize)
text(0.5,8,'Note: natural cubic fit does not match slope','FontSize',fontsize);
text(0.5,7.4,'of extrapolated function values','FontSize',fontsize);


% 6: extrapolate BOTH, interpolate CUBIC
subplot(3,2,6)
plot_points(iv, dv, fontsize);
hold on;
x=min(ivx):0.2:max(ivx);
y=clamped_cubic(x,iv,dv);
% clamped cubic
plot(x,y,'-');
% extrapolation slope lines either end
plot(iv([1 2]),dv([1 2]),'g-.')
plot(iv([n-1 n]),dv([n-1 n]),'g-.')
hold off;
text(labelx,label1y,'extrapolate="both"','FontSize',fontsize);
text(labelx,label2y,'interpolate="cubic"','FontSize',fontsize);
text(0.5,8,'Note: clamped cubic fit matches slope','FontSize',fontsize);
text(0.5,7.4,'of extrapolated function values','FontSize',fontsize);



