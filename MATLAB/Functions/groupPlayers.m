function f = groupPlayers(x,scores,clusterNumber,clusterSize,...
    Players,ParticipantIndex,settings)

teamScore = getTeamScore(x,scores,clusterNumber,clusterSize,...
    Players,ParticipantIndex,settings,1);   % Get team scores

f = sum(teamScore.^2);  % Define objective function
end