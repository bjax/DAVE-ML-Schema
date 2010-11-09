function y = clamped_cubic(x, xk, yk)
% Y = CLAMPED_CUBIC(X, XK, YK)
%
% Generate relaxed cubic spline fit with ends clamped to slope of initial
% and final points (clamped).
%
% Returns column vector Y with cubic-interpolated (or linear-extracted)
% points for given input vector X, with piece-wise cubic segments that
% match 0, 1, and 2nd derivatives at knots represented by (XK, YK).

% 2008-10-16 Bruce Jackson, NASA Langley <bruce.jackson@nasa.gov>
%%%

% Segments are numbered 1, 2, 3, etc. corresponding with the lower (right)
% knot x(i), y(i).
%
% Derivation found in LiveMath document 'cubic' in
% Projects/Sim_Tech/DAVE/Clamped Splines folder 
%                                                                                                                                                                
% Numerically solves                                                                                                                                             
%                                                                                                                                                                
%     z = M k
% 
% for vector k of cubic polynomial coefficients
%                                                                                                                                                                
% +-   -+       +-                                                                                                                                -+             
% |  u  |       |  1       0        0       0        0        0     . . .                        0                                          0      |  +-       -+
% |dy(2)|       |dx(2)  dx(2)^2  dx(2)^3    0        0        0     . . .                                                                          |  |   b(1)  |
% |  0  |       |  1   2dx(2)   3dx(2)^2   -1        0        0     . . .                        .                                                 |  |   c(1)  |
% |  0  |       |  0       1    3dx(2)      0       -1        0       0                          .                                                 |  |   d(1)  |
% |dy(3)|       |  0       0        0     dx(3)   dx(3)^2  dx(3)^3    0                          .                               .                 |  |   b(2)  |
% |  0  |       |  .       0        0       1    2dx(3)   3dx(3)^2   -1                                                      .                     |  |   c(2)  |
% |  0  |       |  .       .        0       0        1    3dx(3)      0 -1                                               .                         |  |   d(2)  |
%    .                     .                                                                                                                          |    .    |
%    .      =                                                                                                                                         |    .    |
% |dy(i)|       |                                                              dx(i)  dx(i)^2  dx(i)^3    0        0                               |  |    .    |
% |  0  |       |  0     . . .                                                   1   2dx(i)   3dx(i)^2   -1        0                        0      |  |  b(n-1) |
% |  0  |       |                                                                0       1    3dx(i)      0       -1                               |  |  c(n-1) |
%    .                                                                                                                                                |  d(n-1) |
%    .                                                                                                                                                +-       -+
% |dy(n)|       |  0     . . .                                                                   0                        dx(n)  dx(n)^2  dx(n)^3  |
% |  v  |       |                                                                                                           1   2dx(n)   3dx(n)^2  |
% +-   -+       +-                                                                                                                                -+
%
% where
%  dx(i) = x(i)-x(i-1)
%  dy(i) = y(i)-y(i-1)
%  u = (y(2)-y( 1 ))/(x(2)-x( 1 ))  [slope of first two points]
%  v = (y(n)-y(n-1))/(x(n)-x(n-1))  [slope of last two points]
% 
% and
%  y = y(i) + b(i)*(x - x(i)) + c(i)*(x - x(i))^2 + d(i)*(x - x(i))^3
%     for segment between x(i) <= x <= x(i+1)
%
    
% for now, assume all supplied information is correct (xk, yk have same
% size and xk is monotomically increasing) - add checks later

% number of knots
n = length(xk);

% build dx and dy vectors
dx = [0 diff(xk)];
dy = [0 diff(yk)];

% calculate slopes
u = dy(2)/dx(2);
v = dy(n)/dx(n);

% form z vector
z = u;  % start with initial slope
for i=2:(n-1)  % add three elements for each intermediate knot
    z=[z;dy(i);0;0];
end
z = [z;dy(n);v];  % append last two items

% form matrix M
M = eye(3*(n-1));  % will replace all but first diagonal element shortly
for i=2:(n-1)
    M([1 2 3]+(i-2)*3+1,[1 2 3 4 5]+(i-2)*3) = ...
        [dx(i)    dx(i)^2   dx(i)^3  0   0
           1    2*dx(i)   3*dx(i)^2 -1   0
           0       1      3*dx(i)    0  -1];
end
M([3*(n-1)-1 3*(n-1)],[3*(n-1)-2 3*(n-1)-1 3*(n-1)]) = ...
    [dx(n)  dx(n)^2   dx(n)^3
      1   2*dx(n)   3*dx(n)^2];

% solve for coefficients
k = M\z;
for i=1:(n-1)
    b(i) = k(3*(i-1)+1);
    c(i) = k(3*(i-1)+2);
    d(i) = k(3*(i-1)+3);
end

% now determine in which each argument lies, and either solve appropriate
% cubic polynomial (if inside limits) or extrapolate (if outside)

y = [];
for i=1:numel(x);
    if x(i) < xk(1)
        % extrapolate before first segment
        y = [y; yk(1)+u*(x(i)-xk(1))];
    elseif x(i) > xk(n)
        % extrapolate after last segment
        y = [y; yk(n)+v*(x(i)-xk(n))];
    else
        % find appropriate segment
        for j=1:(n-1)
            if and(x(i) >= xk(j),x(i) < xk(j+1))
                break
            end
        end
        y = [y; yk(j) + ...
                b(j)*(x(i)-xk(j)) + ...
                c(j)*(x(i)-xk(j))^2 + ...
                d(j)*(x(i)-xk(j))^3 ];
    end
end
