function f = groupPlayers(x, scores, clusterNumber, clusterSize)

teamScore = getTeamScore(x, scores, clusterNumber, clusterSize);   % Get team scores

f = sum(teamScore.^2);  % Define objective function
end