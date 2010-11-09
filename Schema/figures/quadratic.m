function y = quadratic(b1, x, xk, yk)
% Y = QUADRATIC(B, X, XK, YK)
%
% Generate quadratic spline fit with given initial slope.
%
% Returns column vector Y with quadratic-interpolated (or
% linear-extracted) points for given input vector X, with piece-wise
% quadratic segments that match value and first derivatives at knots
% represented by (XK, YK), with an initial slope of B. Changing B will
% result in various curves through the knots.

% 2008-10-20 Bruce Jackson, NASA Langley <bruce.jackson@nasa.gov>
%%%

% Segments are numbered 1, 2, 3, etc. corresponding with the lower (right)
% knot x(i), y(i).
%
% Derivation found in LiveMath document 'quadratic' and 'quadratic_with_B1' in
% Projects/Sim_Tech/DAVE/Clamped Splines folder 
%                                                                                                                                                                
% Numerically solves                                                                                                                                             
%                                                                                                                                                                
%     z = M k
% 
% for vector k of cubic polynomial coefficients
%                                                                                                                                                               
% +-              -+     +-                                                                                                                            -+             
% |dy(1)-b(1)*dx(1)|     | dx(1)^2   0        0       0        0        0     . . .               0                                              0      |  
% |      b(1)      |     |-2*dx(1)   1        0       0        0        0     . . .                                                                     |  +-       -+
% |     dy(2)      |     |  0      dx(2)   dx(2)^2    0        0        0     . . .               .                                         .           |  |   c(1)  |
% |       0        |     |  0       -1    -2*dx(2)    1        0        0       0                 .                                    .                |  |   b(2)  |
% |     dy(3)      |     |  0        0        0     dx(3)     dx(3)^2   0       0                 .                               .                     |  |   c(2)  |
%         .               .                                                                                                                                |    .    |
%         .         =                                                                                                                                      |    .    |
% |       0        |     |                                                            -1   -2*dx(i-1)   -1      0                                       |  |    .    |
% |     dy(i)      |     |                                                             0        0      dx(i)  dx(i)^2                                   |  |  b(n-1) |
%         .                                                                                                                                                |  c(n-1) |
%         .                                                                                                                                                +-       -+
% |       0        |     |                                                                        .                       -1  -2*dx(n-1)  -1      0     |
% |     dy(n)      |     |  0     . . .                                                           0                        0       0     dx(n)  dx(n)^2 |
% +-              -+     +-                                                                                                                            -+
%
% where
%  dx(i) = x(i)-x(i-1)
%  dy(i) = y(i)-y(i-1)
% 
% and
%  y = y(i) + b(i)*(x - x(i)) + c(i)*(x - x(i))^2
%     for segment between x(i) <= x <= x(i+1)
%
    
% for now, assume all supplied information is correct (xk, yk have same
% size and xk is monotomically increasing) - need to add checks

% number of knots
n = length(xk);

% build dx and dy vectors
dx = [diff(xk) 0];
dy = [diff(yk) 0];

% calculate slopes
u = dy(1)/dx(1);
v = dy(n-1)/dx(n-1);

% form z vector
z = [dy(1)-b1*dx(1); b1; dy(2)];  % start with initial slope

% form matrix M
M = eye(2*n-3);  % will replace all diagonal elements

% first three rows
M(1:3,1:3) = [dx(1)^2 0 0;-2*dx(1) 1 0; 0 dx(2) dx(2)^2];
for i=3:(n-1)
    z=[z;0;dy(i)];
    M([1 2]+(i-2)*2+1,[1 2 3 4]+(i-2)*2-1) = ...
        [-1  -2*dx(i-1)   1        0
          0      0      dx(i)  dx(i)^2];
end

% solve for coefficients
k = M\z;
b(1)=b1;
c(1)=k(1);
for i=2:(n-1)
    b(i) = k(2*(i-1));
    c(i) = k(2*(i-1)+1);
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
                c(j)*(x(i)-xk(j))^2 ];
    end
end