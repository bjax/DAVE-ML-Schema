function [success] = F16_aero_verify()
% Script file to verify implementation of F16_aero.xml in Simulink
%
% Created 02-MAR-04 by DAVE2SL utility
%

% set filename

% Load data file - no longer needed with model workspace storage
%F16_aero_setup

% Set options
options = simset('FixedStep',1,'MaxStep',1,'MinStep',1,'Solver','FixedStepDiscrete','SrcWorkspace','current');

%
% Checkcase data
%

% Checkcase 1 - Nominal

checkcase{1}.name = 'Nominal';

checkcase{1}.u = [
300.0	% trueAirspeed
5.0	% angleOfAttack
0.0	% angleOfSideslip
0.0	% bodyAngularRate_Roll
0.0	% bodyAngularRate_Pitch
0.0	% bodyAngularRate_Yaw
0.0	% elevatorDeflection
0.0	% aileronDeflection
0.0	% rudderDeflection
0.25	% bodyPositionOfCgWrtWingLeadingEdge_X
     ]';

checkcase{1}.y = [
-0.0040	% aeroBodyForceCoefficient_X
0.0	% aeroBodyForceCoefficient_Y
-0.416	% aeroBodyForceCoefficient_Z
0.0	% aeroBodyMomentCoefficient_Roll
-0.0466	% aeroBodyMomentCoefficient_Pitch
0.0	% aeroBodyMomentCoefficient_Yaw
     ]';

checkcase{1}.tol = [
1.0E-6	% aeroBodyForceCoefficient_X
1.0E-6	% aeroBodyForceCoefficient_Y
1.0E-6	% aeroBodyForceCoefficient_Z
1.0E-6	% aeroBodyMomentCoefficient_Roll
1.0E-6	% aeroBodyMomentCoefficient_Pitch
1.0E-6	% aeroBodyMomentCoefficient_Yaw
     ]';


% Checkcase 2 - Positive sideslip

checkcase{2}.name = 'Positive sideslip';

checkcase{2}.u = [
300.0	% trueAirspeed
5.0	% angleOfAttack
2.34	% angleOfSideslip
0.0	% bodyAngularRate_Roll
0.0	% bodyAngularRate_Pitch
0.0	% bodyAngularRate_Yaw
0.0	% elevatorDeflection
0.0	% aileronDeflection
0.0	% rudderDeflection
0.25	% bodyPositionOfCgWrtWingLeadingEdge_X
     ]';

checkcase{2}.y = [
-0.0040	% aeroBodyForceCoefficient_X
-0.0468	% aeroBodyForceCoefficient_Y
-0.41530612733219	% aeroBodyForceCoefficient_Z
-0.005616	% aeroBodyMomentCoefficient_Roll
-0.04653061273322	% aeroBodyMomentCoefficient_Pitch
0.01065792	% aeroBodyMomentCoefficient_Yaw
     ]';

checkcase{2}.tol = [
1.0E-6	% aeroBodyForceCoefficient_X
1.0E-6	% aeroBodyForceCoefficient_Y
1.0E-6	% aeroBodyForceCoefficient_Z
1.0E-6	% aeroBodyMomentCoefficient_Roll
1.0E-6	% aeroBodyMomentCoefficient_Pitch
1.0E-6	% aeroBodyMomentCoefficient_Yaw
     ]';


% Checkcase 3 - Negative sideslip

checkcase{3}.name = 'Negative sideslip';

checkcase{3}.u = [
300.0	% trueAirspeed
5.0	% angleOfAttack
-2.34	% angleOfSideslip
0.0	% bodyAngularRate_Roll
0.0	% bodyAngularRate_Pitch
0.0	% bodyAngularRate_Yaw
0.0	% elevatorDeflection
0.0	% aileronDeflection
0.0	% rudderDeflection
0.25	% bodyPositionOfCgWrtWingLeadingEdge_X
     ]';

checkcase{3}.y = [
-0.0040	% aeroBodyForceCoefficient_X
0.0468	% aeroBodyForceCoefficient_Y
-0.41530612733219	% aeroBodyForceCoefficient_Z
0.005616	% aeroBodyMomentCoefficient_Roll
-0.04653061273322	% aeroBodyMomentCoefficient_Pitch
-0.01065792	% aeroBodyMomentCoefficient_Yaw
     ]';

checkcase{3}.tol = [
1.0E-6	% aeroBodyForceCoefficient_X
1.0E-6	% aeroBodyForceCoefficient_Y
1.0E-6	% aeroBodyForceCoefficient_Z
1.0E-6	% aeroBodyMomentCoefficient_Roll
1.0E-6	% aeroBodyMomentCoefficient_Pitch
1.0E-6	% aeroBodyMomentCoefficient_Yaw
     ]';


% Checkcase 4 - Positive roll rate

checkcase{4}.name = 'Positive roll rate';

checkcase{4}.u = [
300.0	% trueAirspeed
5.0	% angleOfAttack
0.0	% angleOfSideslip
3.42	% bodyAngularRate_Roll
0.0	% bodyAngularRate_Pitch
0.0	% bodyAngularRate_Yaw
0.0	% elevatorDeflection
0.0	% aileronDeflection
0.0	% rudderDeflection
0.25	% bodyPositionOfCgWrtWingLeadingEdge_X
     ]';

checkcase{4}.y = [
-0.0040	% aeroBodyForceCoefficient_X
0.01881	% aeroBodyForceCoefficient_Y
-0.416	% aeroBodyForceCoefficient_Z
-0.07182	% aeroBodyMomentCoefficient_Roll
-0.0466	% aeroBodyMomentCoefficient_Pitch
-0.002761764	% aeroBodyMomentCoefficient_Yaw
     ]';

checkcase{4}.tol = [
1.0E-6	% aeroBodyForceCoefficient_X
1.0E-6	% aeroBodyForceCoefficient_Y
1.0E-6	% aeroBodyForceCoefficient_Z
1.0E-6	% aeroBodyMomentCoefficient_Roll
1.0E-6	% aeroBodyMomentCoefficient_Pitch
1.0E-6	% aeroBodyMomentCoefficient_Yaw
     ]';


% Checkcase 5 - Negative roll rate

checkcase{5}.name = 'Negative roll rate';

checkcase{5}.u = [
300.0	% trueAirspeed
5.0	% angleOfAttack
0.0	% angleOfSideslip
-3.42	% bodyAngularRate_Roll
0.0	% bodyAngularRate_Pitch
0.0	% bodyAngularRate_Yaw
0.0	% elevatorDeflection
0.0	% aileronDeflection
0.0	% rudderDeflection
0.25	% bodyPositionOfCgWrtWingLeadingEdge_X
     ]';

checkcase{5}.y = [
-0.0040	% aeroBodyForceCoefficient_X
-0.01881	% aeroBodyForceCoefficient_Y
-0.416	% aeroBodyForceCoefficient_Z
0.07182	% aeroBodyMomentCoefficient_Roll
-0.0466	% aeroBodyMomentCoefficient_Pitch
0.002761764	% aeroBodyMomentCoefficient_Yaw
     ]';

checkcase{5}.tol = [
1.0E-6	% aeroBodyForceCoefficient_X
1.0E-6	% aeroBodyForceCoefficient_Y
1.0E-6	% aeroBodyForceCoefficient_Z
1.0E-6	% aeroBodyMomentCoefficient_Roll
1.0E-6	% aeroBodyMomentCoefficient_Pitch
1.0E-6	% aeroBodyMomentCoefficient_Yaw
     ]';


% Checkcase 6 - Positive pitch rate

checkcase{6}.name = 'Positive pitch rate';

checkcase{6}.u = [
300.0	% trueAirspeed
5.0	% angleOfAttack
0.0	% angleOfSideslip
0.0	% bodyAngularRate_Roll
0.98	% bodyAngularRate_Pitch
0.0	% bodyAngularRate_Yaw
0.0	% elevatorDeflection
0.0	% aileronDeflection
0.0	% rudderDeflection
0.25	% bodyPositionOfCgWrtWingLeadingEdge_X
     ]';

checkcase{6}.y = [
0.02077570666667	% aeroBodyForceCoefficient_X
0.0	% aeroBodyForceCoefficient_Y
-0.99656506666667	% aeroBodyForceCoefficient_Z
0.0	% aeroBodyMomentCoefficient_Roll
-0.2019104	% aeroBodyMomentCoefficient_Pitch
0.0	% aeroBodyMomentCoefficient_Yaw
     ]';

checkcase{6}.tol = [
1.0E-6	% aeroBodyForceCoefficient_X
1.0E-6	% aeroBodyForceCoefficient_Y
1.0E-6	% aeroBodyForceCoefficient_Z
1.0E-6	% aeroBodyMomentCoefficient_Roll
1.0E-6	% aeroBodyMomentCoefficient_Pitch
1.0E-6	% aeroBodyMomentCoefficient_Yaw
     ]';


% Checkcase 7 - Negative pitch rate

checkcase{7}.name = 'Negative pitch rate';

checkcase{7}.u = [
300.0	% trueAirspeed
5.0	% angleOfAttack
0.0	% angleOfSideslip
0.0	% bodyAngularRate_Roll
-0.98	% bodyAngularRate_Pitch
0.0	% bodyAngularRate_Yaw
0.0	% elevatorDeflection
0.0	% aileronDeflection
0.0	% rudderDeflection
0.25	% bodyPositionOfCgWrtWingLeadingEdge_X
     ]';

checkcase{7}.y = [
-0.02877570666667	% aeroBodyForceCoefficient_X
0.0	% aeroBodyForceCoefficient_Y
0.16456506666667	% aeroBodyForceCoefficient_Z
0.0	% aeroBodyMomentCoefficient_Roll
0.1087104	% aeroBodyMomentCoefficient_Pitch
0.0	% aeroBodyMomentCoefficient_Yaw
     ]';

checkcase{7}.tol = [
1.0E-6	% aeroBodyForceCoefficient_X
1.0E-6	% aeroBodyForceCoefficient_Y
1.0E-6	% aeroBodyForceCoefficient_Z
1.0E-6	% aeroBodyMomentCoefficient_Roll
1.0E-6	% aeroBodyMomentCoefficient_Pitch
1.0E-6	% aeroBodyMomentCoefficient_Yaw
     ]';


% Checkcase 8 - Positive pitch rate

checkcase{8}.name = 'Positive pitch rate';

checkcase{8}.u = [
300.0	% trueAirspeed
5.0	% angleOfAttack
0.0	% angleOfSideslip
0.0	% bodyAngularRate_Roll
0.0	% bodyAngularRate_Pitch
2.92	% bodyAngularRate_Yaw
0.0	% elevatorDeflection
0.0	% aileronDeflection
0.0	% rudderDeflection
0.25	% bodyPositionOfCgWrtWingLeadingEdge_X
     ]';

checkcase{8}.y = [
-0.0040	% aeroBodyForceCoefficient_X
0.139868	% aeroBodyForceCoefficient_Y
-0.416	% aeroBodyForceCoefficient_Z
0.016498	% aeroBodyMomentCoefficient_Roll
-0.0466	% aeroBodyMomentCoefficient_Pitch
-0.06163368586667	% aeroBodyMomentCoefficient_Yaw
     ]';

checkcase{8}.tol = [
1.0E-6	% aeroBodyForceCoefficient_X
1.0E-6	% aeroBodyForceCoefficient_Y
1.0E-6	% aeroBodyForceCoefficient_Z
1.0E-6	% aeroBodyMomentCoefficient_Roll
1.0E-6	% aeroBodyMomentCoefficient_Pitch
1.0E-6	% aeroBodyMomentCoefficient_Yaw
     ]';


% Checkcase 9 - Negative pitch rate

checkcase{9}.name = 'Negative pitch rate';

checkcase{9}.u = [
300.0	% trueAirspeed
5.0	% angleOfAttack
0.0	% angleOfSideslip
0.0	% bodyAngularRate_Roll
0.0	% bodyAngularRate_Pitch
-2.92	% bodyAngularRate_Yaw
0.0	% elevatorDeflection
0.0	% aileronDeflection
0.0	% rudderDeflection
0.25	% bodyPositionOfCgWrtWingLeadingEdge_X
     ]';

checkcase{9}.y = [
-0.0040	% aeroBodyForceCoefficient_X
-0.139868	% aeroBodyForceCoefficient_Y
-0.416	% aeroBodyForceCoefficient_Z
-0.016498	% aeroBodyMomentCoefficient_Roll
-0.0466	% aeroBodyMomentCoefficient_Pitch
0.06163368586667	% aeroBodyMomentCoefficient_Yaw
     ]';

checkcase{9}.tol = [
1.0E-6	% aeroBodyForceCoefficient_X
1.0E-6	% aeroBodyForceCoefficient_Y
1.0E-6	% aeroBodyForceCoefficient_Z
1.0E-6	% aeroBodyMomentCoefficient_Roll
1.0E-6	% aeroBodyMomentCoefficient_Pitch
1.0E-6	% aeroBodyMomentCoefficient_Yaw
     ]';


% Checkcase 10 - Positive elevator

checkcase{10}.name = 'Positive elevator';

checkcase{10}.u = [
300.0	% trueAirspeed
5.0	% angleOfAttack
0.0	% angleOfSideslip
0.0	% bodyAngularRate_Roll
0.0	% bodyAngularRate_Pitch
0.0	% bodyAngularRate_Yaw
12.92	% elevatorDeflection
0.0	% aileronDeflection
0.0	% rudderDeflection
0.25	% bodyPositionOfCgWrtWingLeadingEdge_X
     ]';

checkcase{10}.y = [
-0.02860333333333	% aeroBodyForceCoefficient_X
0.0	% aeroBodyForceCoefficient_Y
-0.514192	% aeroBodyForceCoefficient_Z
0.0	% aeroBodyMomentCoefficient_Roll
-0.1834792	% aeroBodyMomentCoefficient_Pitch
0.0	% aeroBodyMomentCoefficient_Yaw
     ]';

checkcase{10}.tol = [
1.0E-6	% aeroBodyForceCoefficient_X
1.0E-6	% aeroBodyForceCoefficient_Y
1.0E-6	% aeroBodyForceCoefficient_Z
1.0E-6	% aeroBodyMomentCoefficient_Roll
1.0E-6	% aeroBodyMomentCoefficient_Pitch
1.0E-6	% aeroBodyMomentCoefficient_Yaw
     ]';


% Checkcase 11 - Negative elevator

checkcase{11}.name = 'Negative elevator';

checkcase{11}.u = [
300.0	% trueAirspeed
5.0	% angleOfAttack
0.0	% angleOfSideslip
0.0	% bodyAngularRate_Roll
0.0	% bodyAngularRate_Pitch
0.0	% bodyAngularRate_Yaw
-12.92	% elevatorDeflection
0.0	% aileronDeflection
0.0	% rudderDeflection
0.25	% bodyPositionOfCgWrtWingLeadingEdge_X
     ]';

checkcase{11}.y = [
-0.02422	% aeroBodyForceCoefficient_X
0.0	% aeroBodyForceCoefficient_Y
-0.317808	% aeroBodyForceCoefficient_Z
0.0	% aeroBodyMomentCoefficient_Roll
0.08481253333333	% aeroBodyMomentCoefficient_Pitch
0.0	% aeroBodyMomentCoefficient_Yaw
     ]';

checkcase{11}.tol = [
1.0E-6	% aeroBodyForceCoefficient_X
1.0E-6	% aeroBodyForceCoefficient_Y
1.0E-6	% aeroBodyForceCoefficient_Z
1.0E-6	% aeroBodyMomentCoefficient_Roll
1.0E-6	% aeroBodyMomentCoefficient_Pitch
1.0E-6	% aeroBodyMomentCoefficient_Yaw
     ]';


% Checkcase 12 - Positive aileron

checkcase{12}.name = 'Positive aileron';

checkcase{12}.u = [
300.0	% trueAirspeed
5.0	% angleOfAttack
0.0	% angleOfSideslip
0.0	% bodyAngularRate_Roll
0.0	% bodyAngularRate_Pitch
0.0	% bodyAngularRate_Yaw
0.0	% elevatorDeflection
24.1	% aileronDeflection
0.0	% rudderDeflection
0.25	% bodyPositionOfCgWrtWingLeadingEdge_X
     ]';

checkcase{12}.y = [
-0.0040	% aeroBodyForceCoefficient_X
0.025305	% aeroBodyForceCoefficient_Y
-0.416	% aeroBodyForceCoefficient_Z
-0.06266	% aeroBodyMomentCoefficient_Roll
-0.0466	% aeroBodyMomentCoefficient_Pitch
-0.011799842	% aeroBodyMomentCoefficient_Yaw
     ]';

checkcase{12}.tol = [
1.0E-6	% aeroBodyForceCoefficient_X
1.0E-6	% aeroBodyForceCoefficient_Y
1.0E-6	% aeroBodyForceCoefficient_Z
1.0E-6	% aeroBodyMomentCoefficient_Roll
1.0E-6	% aeroBodyMomentCoefficient_Pitch
1.0E-6	% aeroBodyMomentCoefficient_Yaw
     ]';


% Checkcase 13 - Negative aileron

checkcase{13}.name = 'Negative aileron';

checkcase{13}.u = [
300.0	% trueAirspeed
5.0	% angleOfAttack
0.0	% angleOfSideslip
0.0	% bodyAngularRate_Roll
0.0	% bodyAngularRate_Pitch
0.0	% bodyAngularRate_Yaw
0.0	% elevatorDeflection
-24.1	% aileronDeflection
0.0	% rudderDeflection
0.25	% bodyPositionOfCgWrtWingLeadingEdge_X
     ]';

checkcase{13}.y = [
-0.0040	% aeroBodyForceCoefficient_X
-0.025305	% aeroBodyForceCoefficient_Y
-0.416	% aeroBodyForceCoefficient_Z
0.06266	% aeroBodyMomentCoefficient_Roll
-0.0466	% aeroBodyMomentCoefficient_Pitch
0.011799842	% aeroBodyMomentCoefficient_Yaw
     ]';

checkcase{13}.tol = [
1.0E-6	% aeroBodyForceCoefficient_X
1.0E-6	% aeroBodyForceCoefficient_Y
1.0E-6	% aeroBodyForceCoefficient_Z
1.0E-6	% aeroBodyMomentCoefficient_Roll
1.0E-6	% aeroBodyMomentCoefficient_Pitch
1.0E-6	% aeroBodyMomentCoefficient_Yaw
     ]';


% Checkcase 14 - Positive rudder

checkcase{14}.name = 'Positive rudder';

checkcase{14}.u = [
300.0	% trueAirspeed
5.0	% angleOfAttack
0.0	% angleOfSideslip
0.0	% bodyAngularRate_Roll
0.0	% bodyAngularRate_Pitch
0.0	% bodyAngularRate_Yaw
0.0	% elevatorDeflection
0.0	% aileronDeflection
12.03	% rudderDeflection
0.25	% bodyPositionOfCgWrtWingLeadingEdge_X
     ]';

checkcase{14}.y = [
-0.0040	% aeroBodyForceCoefficient_X
0.034486	% aeroBodyForceCoefficient_Y
-0.416	% aeroBodyForceCoefficient_Z
0.005614	% aeroBodyMomentCoefficient_Roll
-0.0466	% aeroBodyMomentCoefficient_Pitch
-0.01934627173333	% aeroBodyMomentCoefficient_Yaw
     ]';

checkcase{14}.tol = [
1.0E-6	% aeroBodyForceCoefficient_X
1.0E-6	% aeroBodyForceCoefficient_Y
1.0E-6	% aeroBodyForceCoefficient_Z
1.0E-6	% aeroBodyMomentCoefficient_Roll
1.0E-6	% aeroBodyMomentCoefficient_Pitch
1.0E-6	% aeroBodyMomentCoefficient_Yaw
     ]';


% Checkcase 15 - Negative rudder

checkcase{15}.name = 'Negative rudder';

checkcase{15}.u = [
300.0	% trueAirspeed
5.0	% angleOfAttack
0.0	% angleOfSideslip
0.0	% bodyAngularRate_Roll
0.0	% bodyAngularRate_Pitch
0.0	% bodyAngularRate_Yaw
0.0	% elevatorDeflection
0.0	% aileronDeflection
-12.03	% rudderDeflection
0.25	% bodyPositionOfCgWrtWingLeadingEdge_X
     ]';

checkcase{15}.y = [
-0.0040	% aeroBodyForceCoefficient_X
-0.034486	% aeroBodyForceCoefficient_Y
-0.416	% aeroBodyForceCoefficient_Z
-0.005614	% aeroBodyMomentCoefficient_Roll
-0.0466	% aeroBodyMomentCoefficient_Pitch
0.01934627173333	% aeroBodyMomentCoefficient_Yaw
     ]';

checkcase{15}.tol = [
1.0E-6	% aeroBodyForceCoefficient_X
1.0E-6	% aeroBodyForceCoefficient_Y
1.0E-6	% aeroBodyForceCoefficient_Z
1.0E-6	% aeroBodyMomentCoefficient_Roll
1.0E-6	% aeroBodyMomentCoefficient_Pitch
1.0E-6	% aeroBodyMomentCoefficient_Yaw
     ]';


% Checkcase 16 - Aft CG

checkcase{16}.name = 'Aft CG';

checkcase{16}.u = [
300.0	% trueAirspeed
5.0	% angleOfAttack
0.0	% angleOfSideslip
0.0	% bodyAngularRate_Roll
0.0	% bodyAngularRate_Pitch
0.0	% bodyAngularRate_Yaw
0.0	% elevatorDeflection
0.0	% aileronDeflection
0.0	% rudderDeflection
0.35	% bodyPositionOfCgWrtWingLeadingEdge_X
     ]';

checkcase{16}.y = [
-0.0040	% aeroBodyForceCoefficient_X
0.0	% aeroBodyForceCoefficient_Y
-0.416	% aeroBodyForceCoefficient_Z
0.0	% aeroBodyMomentCoefficient_Roll
-0.0050	% aeroBodyMomentCoefficient_Pitch
0.0	% aeroBodyMomentCoefficient_Yaw
     ]';

checkcase{16}.tol = [
1.0E-6	% aeroBodyForceCoefficient_X
1.0E-6	% aeroBodyForceCoefficient_Y
1.0E-6	% aeroBodyForceCoefficient_Z
1.0E-6	% aeroBodyMomentCoefficient_Roll
1.0E-6	% aeroBodyMomentCoefficient_Pitch
1.0E-6	% aeroBodyMomentCoefficient_Yaw
     ]';


% Checkcase 17 - Skewed inputs

checkcase{17}.name = 'Skewed inputs';

checkcase{17}.u = [
300.0	% trueAirspeed
16.2	% angleOfAttack
-3.24	% angleOfSideslip
0.56	% bodyAngularRate_Roll
-0.76	% bodyAngularRate_Pitch
-0.94	% bodyAngularRate_Yaw
4.567	% elevatorDeflection
7.654	% aileronDeflection
-2.991	% rudderDeflection
0.123	% bodyPositionOfCgWrtWingLeadingEdge_X
     ]';

checkcase{17}.y = [
0.04794994533333	% aeroBodyForceCoefficient_X
0.02735386	% aeroBodyForceCoefficient_Y
-0.72934852554344	% aeroBodyForceCoefficient_Z
-0.026917840128	% aeroBodyMomentCoefficient_Roll
-0.10638585796503	% aeroBodyMomentCoefficient_Pitch
0.01118365476765	% aeroBodyMomentCoefficient_Yaw
     ]';

checkcase{17}.tol = [
1.0E-6	% aeroBodyForceCoefficient_X
1.0E-6	% aeroBodyForceCoefficient_Y
1.0E-6	% aeroBodyForceCoefficient_Z
1.0E-6	% aeroBodyMomentCoefficient_Roll
1.0E-6	% aeroBodyMomentCoefficient_Pitch
1.0E-6	% aeroBodyMomentCoefficient_Yaw
     ]';


num_cases = 17;

fprintf('Running %d verification cases for %s:\n', num_cases, 'F16_aero');

outcome = 0;

for i=1:num_cases
   UT = [0 checkcase{i}.u];
   y_good = checkcase{i}.y;
   y_tol = checkcase{i}.tol;
   [t,x,y] = sim('F16_aero',[0 0],options,UT);
   outcome(i) = (length(y_good) == sum(abs(y-y_good)<y_tol));

   if outcome(i) == 0
      fprintf(' Case %s FAILED.\n', checkcase{i}.name);
   else
      fprintf(' Case %d passed...\n', i);
   end
end

if sum(outcome) == num_cases
   success = 1;
   fprintf('\nAll cases passed: model "F16_aero" verified.\n');
else
   success = 0;
   fprintf('%f%% (%d of %d) FAILED. "F16_aero" not verified!\n', ...
             100*(num_cases-sum(outcome))/num_cases, ...
             num_cases-sum(outcome), num_cases);
end

