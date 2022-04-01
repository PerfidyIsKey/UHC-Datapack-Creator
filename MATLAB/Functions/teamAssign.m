function state = teamAssign(options,state,flag,PlayerName)

if size(state.Score,2) > 1
    msg = getString(message('globaloptim:gaplotcommon:PlotFcnUnavailable','gaplotbestf'));
    title(msg,'interp','none');
    return;
end

[~,bestTeam] = min(state.Score);
x = state.Population(bestTeam,:);
switch flag
    case 'init'
        X = categorical(PlayerName);
        X = reordercats(X,PlayerName);
        barBest = bar(X,x);
        ax = gca;
        ax.XAxis.TickLabelInterpreter = 'none';
        
        ylabel('Team placement','interp','none')
        set(barBest,'Tag','barbest');
        title('Current best team placement','interp','none')
        
    case 'iter'
        
        barBest = findobj(get(gca,'Children'),'Tag','barbest');
        set(barBest,'YData',x)
end
end