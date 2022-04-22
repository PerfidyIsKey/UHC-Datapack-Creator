clear
close all

addpath('Data','Functions','Documents')
load('DataS45A.mat')

Players = RankDetermine(Players,Seasons);

text = RedditText(Players,Seasons);

%% Save to txt file
filename = ['RedditRankings' Seasons(end).Season '.txt'];
fileID = fopen(['Documents\' filename],'w');           % Open document
fprintf(fileID,'%s \n\n',text);         % Rewrite document
fclose(fileID);                         % Close document