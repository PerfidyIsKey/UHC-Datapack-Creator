function string = iptnum2ordinal(number) %#codegen
%IPTNUM2ORDINAL Convert positive integer to ordinal string.
%   STRING  =  IPTNUM2ORDINAL(NUMBER)  converts the positive integer NUMBER
%   into the ordinal text string STRING.   
%
%   Examples
%   --------
%       % Convert the number 4 into the text string 'fourth'.
%       ordstring = iptnum2ordinal(4);
%
%       % Convert the number 23 into the text string '23rd'.
%       ordstring2 = iptnum2ordinal(23);
  
%   Copyright 2004-2013 The MathWorks, Inc.

if number <= 20
  table1 = {'1st' '2nd' '3rd' '4th' '5th' '6th' '7th' ...
            '8th' '9th' '10th' '11th' '12th' '13th' ...
            '14th' '15th' '16th' '17th' ...
            '18th' '19th' '20th'};
  
  string = table1{number};
  
else
  table2 = {'th' 'st' 'nd' 'rd' 'th' 'th' 'th' 'th' 'th' 'th'};
  ones_digit = rem(number, 10);
  string = sprintf('%d%s',number,table2{ones_digit + 1});
end
