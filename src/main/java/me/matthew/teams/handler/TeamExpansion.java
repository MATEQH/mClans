package me.matthew.teams.handler;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import me.matthew.teams.util.Messages;
import me.matthew.teams.util.NumberUtil;
import org.bukkit.entity.Player;

import java.text.DecimalFormat;

public class TeamExpansion extends PlaceholderExpansion {

    @Override
    public String getIdentifier() {
        return "mteams";
    }

    @Override
    public String getAuthor() {
        return "Matthew";
    }

    @Override
    public String getVersion() {
        return "0.1";
    }

    @Override
    public boolean canRegister() {
        return true;
    }

    @Override
    public boolean persist() {
        return true;
    }

    @Override
    public String onPlaceholderRequest(Player player, String params) {
        if (player == null) {
            return "";
        }
        if (params.startsWith("playerteam_")) {
            Team team = TeamHandler.getByPlayer(player);
            if (team == null) {
                return Messages.get(Messages.PLACEHOLDER_NOT_FOUND);
            }
            String param = params.replace("playerteam_", "");
            if (param.equalsIgnoreCase("name")) {
                return team.getName();
            } else if (param.equalsIgnoreCase("balance")) {
                return new DecimalFormat("#.##").format(team.getBalance());
            } else if (param.equalsIgnoreCase("kills")) {
                return String.valueOf(team.getKills());
            } else if (param.equalsIgnoreCase("points")) {
                return String.valueOf(team.getPoints());
            } else if (param.equalsIgnoreCase("position")) {
                return String.valueOf(TeamHandler.getLastSortedTeam().indexOf(team) + 1);
            } else if (param.equalsIgnoreCase("online")) {
                return String.valueOf(team.getOnlineMembers(player).size());
            }
        } else if (params.startsWith("top_")) {
            String param = params.replace("top_", "");
            if (param.endsWith("_team_name")) {
                param = param.replace("_team_name", "");
                if (NumberUtil.isInt(param) && Integer.parseInt(param) > 0 && Integer.parseInt(param) <= TeamHandler.getTeamMap().size()) {
                    return TeamHandler.getLastSortedTeam().get(Integer.parseInt(param) - 1).getName();
                }
                return Messages.get(Messages.PLACEHOLDER_NOT_FOUND).replace("%teamName%", param);
            } else if (param.endsWith("_team_balance")) {
                param = param.replace("_team_balance", "");
                if (NumberUtil.isInt(param) && Integer.parseInt(param) > 0 && Integer.parseInt(param) <= TeamHandler.getTeamMap().size()) {
                    return new DecimalFormat("#.##").format(TeamHandler.getLastSortedTeam().get(Integer.parseInt(param) - 1).getBalance());
                }
                return Messages.get(Messages.PLACEHOLDER_NOT_FOUND).replace("%teamName%", param);
            } else if (param.endsWith("_team_kills")) {
                param = param.replace("_team_kills", "");
                if (NumberUtil.isInt(param) && Integer.parseInt(param) > 0 && Integer.parseInt(param) <= TeamHandler.getTeamMap().size()) {
                    return String.valueOf(TeamHandler.getLastSortedTeam().get(Integer.parseInt(param) - 1).getKills());
                }
                return Messages.get(Messages.PLACEHOLDER_NOT_FOUND).replace("%teamName%", param);
            } else if (param.endsWith("_team_points")) {
                param = param.replace("_team_points", "");
                if (NumberUtil.isInt(param) && Integer.parseInt(param) > 0 && Integer.parseInt(param) <= TeamHandler.getTeamMap().size()) {
                    return String.valueOf(TeamHandler.getLastSortedTeam().get(Integer.parseInt(param) - 1).getPoints());
                }
                return Messages.get(Messages.PLACEHOLDER_NOT_FOUND).replace("%teamName%", param);
            } else if (param.endsWith("_team_online")) {
                param = param.replace("_team_online", "");
                if (NumberUtil.isInt(param) && Integer.parseInt(param) > 0 && Integer.parseInt(param) <= TeamHandler.getTeamMap().size()) {
                    return String.valueOf(TeamHandler.getLastSortedTeam().get(Integer.parseInt(param) - 1).getOnlineMembers(player).size());
                }
                return Messages.get(Messages.PLACEHOLDER_NOT_FOUND).replace("%teamName%", param);
            }
        } else if (params.startsWith("targetteam_")) {
            String param = params.replace("targetteam_", "");
            String[] args = param.split("_");
            String name = args[0];
            Team team = TeamHandler.getByName(name);
            if (team == null) {
                return Messages.get(Messages.PLACEHOLDER_NOT_FOUND);
            }
            param = param.replace(name + "_", "");
            if (param.equalsIgnoreCase("name")) {
                return team.getName();
            } else if (param.equalsIgnoreCase("balance")) {
                return new DecimalFormat("#.##").format(team.getBalance());
            } else if (param.equalsIgnoreCase("kills")) {
                return String.valueOf(team.getKills());
            } else if (param.equalsIgnoreCase("points")) {
                return String.valueOf(team.getPoints());
            } else if (param.equalsIgnoreCase("position")) {
                return String.valueOf(TeamHandler.getLastSortedTeam().indexOf(team) + 1);
            } else if (param.equalsIgnoreCase("online")) {
                return String.valueOf(team.getOnlineMembers(player).size());
            }
        }
        return "";
    }
}
