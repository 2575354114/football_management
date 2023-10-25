package com.lz.football_management.controller;

import com.lz.football_management.dao.PlayerDao;
import com.lz.football_management.dao.TeamDao;
import com.lz.football_management.entity.Player;
import com.lz.football_management.entity.Team;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Controller
public class TeamController {
    @Autowired
    TeamDao teamDao;
    @Autowired
    PlayerDao playerDao;

    @GetMapping(value = "/teams")
    public String listTeam(@RequestParam(value = "name", required = false) String name, Model model) {
        List<Team> teams;
        if (name == null || name.trim().isEmpty()) {
            teams = teamDao.findAllTeams(); // 获取所有球队信息
        } else {
            teams = teamDao.selectTeamsByName(name); // 获取搜索到的球员
        }
        model.addAttribute("teams", teams);
        return "teams";
    }

    @GetMapping("/teams/add")
    public String showAddTeamForm(Model model) {
        model.addAttribute("team", new Team());

        // 获取所有球队信息，并将其添加到 Model 中
        List<Team> allTeams = teamDao.findAllTeams();
        model.addAttribute("allTeams", allTeams);

        return "add-team";
    }

    @PostMapping("/teams/add")
    public String addTeam(Team team, MultipartFile logoFile, HttpServletRequest request, Model model) throws IOException {
        // 获取上传的文件将被存储的目录的路径。
        String path = "/Users/kevin/Downloads/upload/logo/";

        // 检查以确保该路径是一个有效的目录。
        if (!new File(path).exists()) {
            throw new IllegalArgumentException("The path '" + path + "' does not exist.");
        }

        // 检查以确保imgFile参数不是空的。
        if (logoFile == null) {
            throw new IllegalArgumentException("The imgFile parameter cannot be null.");
        }

        // 获取上传文件的原始文件名。
        String name = logoFile.getOriginalFilename();

        // 为上传的文件生成一个独特的文件名。
        String uniqueName = UUID.randomUUID().toString() + name.substring(name.lastIndexOf("."));

        // 为上传的文件创建一个新的文件对象。
        File file = new File(path, uniqueName);

        // 将上传的文件转移到新的文件对象。.
        logoFile.transferTo(file);

        // 将球员对象的img属性设置为唯一的文件名。
        team.setLogo(uniqueName);

        // 将球员对象添加到数据库。
        if (teamDao.addTeam(team) > 0) {
            // 重定向用户到球队名单页。
            return "redirect:/teams";
        } else {
            // 将球员对象添加到模型中，这样就可以在添加球员页面上显示。
            model.addAttribute("team", team);

            // 放回add-player的页面
            return "add-team";
        }
    }


    @GetMapping("/teams/edit/{id}")
    public String showUpdateTeamForm(@PathVariable("id") int id, Model model, HttpSession session) {
        Team team = teamDao.findTeamById(id);
        model.addAttribute("team", team);
        session.setAttribute("logoFile", team.getLogo());
        return "edit-team";
    }


    /**
     * 更新球员信息
     *
     * @return 重定向到球员列表
     */

    @RequestMapping("/teams/update/{id}")
    public String updateTeam(Team team, MultipartFile logoFile, HttpServletRequest request) throws IOException {
        if (!logoFile.isEmpty()) {
            String path = "/Users/kevin/Downloads/upload/logo/";

            String oldName = (String) request.getSession().getAttribute("logoFile");
            if (oldName != null) {
                File oldFile = new File(path, oldName);
                if (oldFile.exists()) {
                    oldFile.delete();
                }
                request.getSession().setAttribute("logoFile", null);
            }

            String name = logoFile.getOriginalFilename();
            name = UUID.randomUUID() + name.substring(name.lastIndexOf("."));
            File newFile = new File(path, name);
            if (!newFile.exists()) newFile.mkdir();
            logoFile.transferTo(newFile);
            team.setLogo(name);
        }
        teamDao.updateTeam(team);

        return "redirect:/teams";
    }


    /**
     * 删除球队
     *
     * @param id 球队ID
     * @return 重定向到球队列表
     **/
    @GetMapping("/teams/delete/{id}")
    public String deleteTeam(@PathVariable("id") int id, Model model) {
        // 查询球队信息
        Team team = teamDao.findTeamById(id);
        // 检查球队是否有球员
        List<Player> players = playerDao.findPlayersByTeamId(id);
        if (!players.isEmpty()) {
            // 添加额外的属性用于显示球队名称
            model.addAttribute("teamName", team.getName());
            return "team_delete_confirmation"; // 转到球队删除确认页面
        }

        // 如果球队没有球员，执行删除操作
        if (team != null) {
            // 检查team是否有图像
            if (team.getLogo() != null) {
                // 为图像创建一个文件对象
                File oldFile = new File("/Users/kevin/Downloads/upload/logo/", team.getLogo());

                // 检查该文件是否存在
                if (oldFile.exists()) {
                    // 删除该文件
                    oldFile.delete();
                }
            }

            // 从数据库中删除该球队
            teamDao.deleteTeamById(id);
        }

        // 重定向用户到球队名单页面
        return "redirect:/teams";
    }

    @PostMapping("/teams/delete/{id}")
    public String forceDeleteTeam(@PathVariable("id") int id, Model model) {
        // 查询球队信息
        Team team = teamDao.findTeamById(id);

        // 删除球队的所有球员
        List<Player> players = playerDao.findPlayersByTeamId(id);
        for (Player player : players) {
            // 检查球员是否有图像
            if (player.getImg() != null) {
                // 为图像创建一个文件对象
                File oldFile = new File("/Users/kevin/Downloads/upload/img/", player.getImg());

                // 检查该文件是否存在
                if (oldFile.exists()) {
                    // 删除该文件
                    oldFile.delete();
                }
            }
        }

        // 删除球队的所有球员记录
        playerDao.deletePlayersByTeamId(id);

        // 删除球队
        if (team != null) {
            // 检查team是否有图像
            if (team.getLogo() != null) {
                // 为图像创建一个文件对象
                File oldFile = new File("/Users/kevin/Downloads/upload/logo/", team.getLogo());

                // 检查该文件是否存在
                if (oldFile.exists()) {
                    // 删除该文件
                    oldFile.delete();
                }
            }

            // 从数据库中删除该球队
            teamDao.deleteTeamById(id);
        }

        return "redirect:/teams"; // 返回到球队列表页面
    }


    @GetMapping("/teams/view/{id}")
    public String showTeamPlayers(@PathVariable("id") int teamId, Model model) {
        List<Player> players = playerDao.findPlayersByTeamId(teamId);
        Team team = teamDao.findTeamById(teamId);
        model.addAttribute("players", players);
        model.addAttribute("teamName", team.getName());
        return "view";


    }
}
