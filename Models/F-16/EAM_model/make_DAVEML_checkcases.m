function make_DAVEML_checkcases
pid = fopen('checkcases.xml','w');
fprintf(pid,'  <checkData>\n');
fprintf(pid,'    <source>From E. A. Morelli''s f16 matlab script, dated 09-Oct-2000;\n');
fprintf(pid,'            Generated using ''make_DAVEML_checkcases.m''</source>\n');
vt = 300.0;
alpha = 5.0;
beta = 0.;
p = 0.;
q = 0.;
r = 0.;
el = 0.0;
ail = 0.0;
rdr = 0.0;
xcg = 0.25;
gen_checkcase(pid, 'Nominal', vt, alpha, beta, p, q, r, el, ail, rdr, xcg)

beta = +2.34; gen_checkcase(pid, 'Positive sideslip', vt, alpha, beta, p, q, r, el, ail, rdr, xcg)
beta = -2.34; gen_checkcase(pid, 'Negative sideslip', vt, alpha, beta, p, q, r, el, ail, rdr, xcg)
beta =  0.0;  

p = +3.42; gen_checkcase(pid, 'Positive roll rate', vt, alpha, beta, p, q, r, el, ail, rdr, xcg)
p = -3.42; gen_checkcase(pid, 'Negative roll rate', vt, alpha, beta, p, q, r, el, ail, rdr, xcg)
p = 0.0;

q = +0.98; gen_checkcase(pid, 'Positive pitch rate', vt, alpha, beta, p, q, r, el, ail, rdr, xcg)
q = -0.98; gen_checkcase(pid, 'Negative pitch rate', vt, alpha, beta, p, q, r, el, ail, rdr, xcg)
q = 0.0;

r = +2.92; gen_checkcase(pid, 'Positive pitch rate', vt, alpha, beta, p, q, r, el, ail, rdr, xcg)
r = -2.92; gen_checkcase(pid, 'Negative pitch rate', vt, alpha, beta, p, q, r, el, ail, rdr, xcg)
r = 0.0;

el = +12.92; gen_checkcase(pid, 'Positive elevator', vt, alpha, beta, p, q, r, el, ail, rdr, xcg)
el = -12.92; gen_checkcase(pid, 'Negative elevator', vt, alpha, beta, p, q, r, el, ail, rdr, xcg)
el = 0.0;

ail = +24.1; gen_checkcase(pid, 'Positive aileron', vt, alpha, beta, p, q, r, el, ail, rdr, xcg)
ail = -24.1; gen_checkcase(pid, 'Negative aileron', vt, alpha, beta, p, q, r, el, ail, rdr, xcg)
ail = 0.0;

rdr = +12.03; gen_checkcase(pid, 'Positive rudder', vt, alpha, beta, p, q, r, el, ail, rdr, xcg)
rdr = -12.03; gen_checkcase(pid, 'Negative rudder', vt, alpha, beta, p, q, r, el, ail, rdr, xcg)
rdr = 0.0;

xcg = 0.35; gen_checkcase(pid, 'Aft CG', vt, alpha, beta, p, q, r, el, ail, rdr, xcg)

alpha = 16.2;
beta = -3.24;
p = 0.56;
q = -0.76;
r = -0.94;
el = 4.567;
ail = 7.654;
rdr = -2.991;
xcg = 0.123;
gen_checkcase(pid, 'Skewed inputs', vt, alpha, beta, p, q, r, el, ail, rdr, xcg)

fprintf(pid,'  </checkData>\n');
fclose(pid);



function gen_checkcase(pid, name, vt, alpha, beta, p, q, r, el, ail, rdr, xcg)
fprintf(pid,'    <staticShot name="%s">\n', name);
fprintf(pid,'      <checkInputs> \n');
fprintf(pid,'	<signal> <signalName>True_Airspeed_f_p_s</signalName>       <signalUnits>ft/sec</signalUnits>  <signalValue>%8.3f</signalValue> </signal>\n', vt);
fprintf(pid,'	<signal> <signalName>Angle_of_Attack_deg</signalName>       <signalUnits>deg</signalUnits>     <signalValue>%8.3f</signalValue> </signal>\n', alpha);
fprintf(pid,'	<signal> <signalName>Angle_of_Sideslip_deg</signalName>     <signalUnits>deg</signalUnits>     <signalValue>%8.3f</signalValue> </signal>\n', beta);
fprintf(pid,'	<signal> <signalName>s_Body_Roll_Rate_rad_p_s</signalName>  <signalUnits>rad/sec</signalUnits> <signalValue>%8.3f</signalValue> </signal>\n', p);
fprintf(pid,'	<signal> <signalName>s_Body_Pitch_Rate_rad_p_s</signalName> <signalUnits>rad/sec</signalUnits> <signalValue>%8.3f</signalValue> </signal>\n', q);
fprintf(pid,'	<signal> <signalName>s_Body_Yaw_Rate_rad_p_s</signalName>   <signalUnits>rad/sec</signalUnits> <signalValue>%8.3f</signalValue> </signal>\n', r);
fprintf(pid,'	<signal> <signalName>delta elevator</signalName>            <signalUnits>deg</signalUnits>     <signalValue>%8.3f</signalValue> </signal>\n', el);
fprintf(pid,'	<signal> <signalName>delta aileron</signalName>             <signalUnits>deg</signalUnits>     <signalValue>%8.3f</signalValue> </signal>\n', ail);
fprintf(pid,'	<signal> <signalName>delta rudder</signalName>              <signalUnits>deg</signalUnits>     <signalValue>%8.3f</signalValue> </signal>\n', rdr);
fprintf(pid,'	<signal> <signalName>Xcg</signalName>                       <signalUnits>fract</signalUnits>   <signalValue>%8.3f</signalValue> </signal>\n', xcg);
fprintf(pid,'      </checkInputs>\n');
f16_aero_setup
[cx,cy,cz,cl,cm,cn] = f16_aero(vt,alpha,beta, p,q,r,el,ail,rdr,xcg);
fprintf(pid,'      <checkOutputs>\n');
fprintf(pid,'	<signal> <signalName>CX</signalName>  <signalValue>%17.14f</signalValue> <tol>0.000001</tol> </signal>\n',cx);
fprintf(pid,'	<signal> <signalName>CY</signalName>  <signalValue>%17.14f</signalValue> <tol>0.000001</tol> </signal>\n',cy);
fprintf(pid,'	<signal> <signalName>CZ</signalName>  <signalValue>%17.14f</signalValue> <tol>0.000001</tol> </signal>\n',cz);
fprintf(pid,'	<signal> <signalName>CLL</signalName> <signalValue>%17.14f</signalValue> <tol>0.000001</tol> </signal>\n',cl);
fprintf(pid,'	<signal> <signalName>CLM</signalName> <signalValue>%17.14f</signalValue> <tol>0.000001</tol> </signal>\n',cm);
fprintf(pid,'	<signal> <signalName>CLN</signalName> <signalValue>%17.14f</signalValue> <tol>0.000001</tol> </signal>\n',cn);
fprintf(pid,'      </checkOutputs>\n');
fprintf(pid,'    </staticShot>\n');
return
