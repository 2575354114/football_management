package com.lz.football_management.controller;


import com.lz.football_management.dao.PlayerDao;
import com.lz.football_management.dao.TeamDao;
import com.lz.football_management.entity.Player;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;


@Controller
public class PlayerController {
    @Autowired
    PlayerDao playerDao;
    @Autowired
    TeamDao teamDao;

    /**
     * 列出球员
     *
     * @param name  球员姓名（可选）
     * @param model 数据模型
     * @return 球员列表视图
     */
    @GetMapping(value = "/players")
    public String listPlayers(@RequestParam(value = "name", required = false) String name, Model model) {
        List<Player> players;
        if (name == null || name.trim().isEmpty()) {
            players = playerDao.findAllPlayersWithTeam(); // 获取所有球员及其所属队伍
        } else {
            players = playerDao.selectPlayersByName(name); // 获取搜索到的球员
        }
        model.addAttribute("players", players);
        return "players";
    }


    /**
     * 根据姓名搜索球员
     *
     * @param name  球员姓名
     * @param model 数据模型
     * @return 球员列表视图
     */
    @GetMapping(value = "/players/search")
    public String searchByName(@RequestParam("name") String name, Model model) {
        List<Player> player = playerDao.selectPlayersByName(name);
        model.addAttribute("player", player);
        return "players";
    }


    /**
     * 显示添加球员表单
     *
     * @param model 数据模型
     * @return 添加球员视图
     */
    @GetMapping("/players/add")
    public String showAddPlayerForm(Model model) {
        model.addAttribute("player", new Player());
        model.addAttribute("allTeams", teamDao.findAllTeams()); // 添加这一行
        return "add-player";
    }






    /**
     * 显示更新球员表单
     *
     * @param id    球员ID
     * @param model 数据模型
     * @return 更新球员视图
     */
    @GetMapping("/players/edit/{id}")
    public String showUpdatePlayerForm(@PathVariable("id") int id, Model model, HttpSession session) {
        Player player = playerDao.findPlayerById(id);
        model.addAttribute("player", player);
        model.addAttribute("allTeams", teamDao.findAllTeams());
        session.setAttribute("imgFile", player.getImg());
        return "edit-player";
    }


    /**
     * 更新球员信息
     *
     * @param player 球员对象
     * @return 重定向到球员列表
     */

    @RequestMapping("/players/update/{id}")
    public String updatePlayer(Player player, MultipartFile imgFile, HttpServletRequest request) throws IOException {
        // 如果上传了照片
        if(!imgFile.isEmpty()) {
            // 项目外上传文件/图像的文件夹
            String path = "/Users/kevin/Downloads/upload/img/";

            // 删除老照片
            String oldName = (String) request.getSession().getAttribute("imgFile");
            if (oldName != null) {
                // Create a File object for the image
                File oldFile = new File(path, oldName);
                // Check if the file exists
                if (oldFile.exists()) {
                    // Delete the file
                    oldFile.delete();
                }
                request.getSession().setAttribute("imgFile", null);
            }

            // 添加新照片
            String name = imgFile.getOriginalFilename();
            name = UUID.randomUUID() + name.substring(name.lastIndexOf("."));
            File newFile = new File(path, name);
            if(!newFile.exists()) newFile.mkdir();
            imgFile.transferTo(newFile);
            player.setImg(name);
        }

        // 更新数据库中的球员
        playerDao.updatePlayer(player);

        // 重定向用户到球员名单页面
        return "redirect:/players";
    }


    /**
     * 删除球员
     *
     * @param id 球员ID
     * @return 重定向到球员列表
     **/
    @GetMapping("/players/delete/{id}")
    public String deletePlayer(@PathVariable("id") int id) {
        Player player = playerDao.findPlayerById(id);

        // 检查Player是否有图像
        if (player.getImg() != null) {
            // 为图像创建一个文件对象
            File oldFile = new File("/Users/kevin/Downloads/upload/img/", player.getImg());

            // 检查该文件是否存在
            if (oldFile.exists()) {
                // 删除该文件
                oldFile.delete();
            }
        }

        // 从数据库中删除该球员
        playerDao.deletePlayerById(id);

        // 重定向用户到球员名单页面
        return "redirect:/players";
    }



    /**
     * 添加学生
     *
     * @param player  球员信息
     * @param imgFile 学生照片
     */
    @PostMapping("/players/add")
    public String addPlayer(Player player, MultipartFile imgFile, HttpServletRequest request, Model model) throws IOException {
        // 获取上传的文件将被存储的目录的路径。
        String path = "/Users/kevin/Downloads/upload/img/";

        // 检查以确保该路径是一个有效的目录。
        if (!new File(path).exists()) {
            throw new IllegalArgumentException("The path '" + path + "' does not exist.");
        }

        // 检查以确保imgFile参数不是空的。
        if (imgFile == null) {
            throw new IllegalArgumentException("The imgFile parameter cannot be null.");
        }

        // 获取上传文件的原始文件名。
        String name = imgFile.getOriginalFilename();

        // 为上传的文件生成一个独特的文件名。
        String uniqueName = UUID.randomUUID().toString() + name.substring(name.lastIndexOf("."));

        // 为上传的文件创建一个新的文件对象。
        File file = new File(path, uniqueName);

        // 将上传的文件转移到新的文件对象。.
        imgFile.transferTo(file);

        // 将球员对象的img属性设置为唯一的文件名。
        player.setImg(uniqueName);

        // 将球员对象添加到数据库。
        if (playerDao.addPlayer(player) > 0) {
            // 重定向用户到球员名单页。
            return "redirect:/players";
        } else {
            // 将球员对象添加到模型中，这样就可以在添加球员页面上显示。
            model.addAttribute("player", player);

            // 放回add-player的页面
            return "add-player";
        }
    }


}