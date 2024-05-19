clear
close all

addpath('Data','Functions','Documents')
load('DataS54.mat')

text = RedditText(Players,Seasons);

%% Save to txt file
filename = ['RedditRankings' Seasons(end).Season '.txt'];
fileID = fopen(['Documents\' filename],'w');           % Open document
fprintf(fileID,'%s \n\n',text);         % Rewrite document
fclose(fileID);                         % Close document