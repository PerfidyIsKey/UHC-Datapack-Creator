function state = teamProgress(options,state,flag,scores,teamNumber,teamSize,...
    Players,ParticipantIndex,settings)

if size(state.Score,2) > 1
    msg = getString(message('globaloptim:gaplotcommon:PlotFcnUnavailable','gaplotbestf'));
    title(msg,'interp','none');
    return;
end

[~,bestTeam] = min(state.Score);
x = state.Population(bestTeam,:);

teamScore = getTeamScore(x,scores,teamNumber,teamSize,...
    Players,ParticipantIndex,settings,2);   % Get team scores
switch flag
    case 'init'
        barBest = bar(teamScore-mean(teamScore));
        yline(settings.rank.UB,'Color','r','LineWidth',1.5)
        yline(-settings.rank.LB,'Color','r','LineWidth',1.5)
        yline(settings.rank.UT,'Color','r','LineStyle','--')
        yline(-settings.rank.LT,'Color','r','LineStyle','--')
        
        ylim([-settings.rank.LT-5,settings.rank.UT+5])
        ylabel('Score Deviation','interp','none')
        xlabel('Team','interp','none')
        set(barBest,'Tag','barscore');
        
    case 'iter'
        
        barBest = findobj(get(gca,'Children'),'Tag','barscore');
        set(barBest,'YData',teamScore-mean(teamScore))
end
end