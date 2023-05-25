package com.example.diary.Form;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.diary.db.Diary;
import com.example.diary.db.DiaryDao;

@Controller
public class DiaryController {

    // 入力画面(localhost:8080/form)
    @RequestMapping("/form")
    public String form(Model model, Form form) {
        return "input";
    }

    // 確認画面(Localhost:8080/confirm)
    @RequestMapping("/confirm")
    public String confirm(Model mode, @Validated Form form, BindingResult result) {
        // エラーがあれば入力画面に渡す
        if (result.hasErrors()) {
            return "input";
        }
        return "confirm";
    }

    // DiaryDaoの保管場所
    private final DiaryDao diarydao;

    // daoを必要な時に呼び出す
    @Autowired
    public DiaryController(DiaryDao diarydao) {
        this.diarydao = diarydao;
    }

    // jpaでinsert
    @RequestMapping("/complete")
    public String complete(Form form, Model mode) {

        // フォームの値をエンティティに入れなおす
        Diary s1 = new Diary();
        s1.setTitle(form.getTitle());
        s1.setContent(form.getContent());

        // JPAでinsert実行
        diarydao.save(s1);

        return "complete";
    }

    // JPA SELECT
    @RequestMapping("/")
    public String index(Model model) {
        // JPA 使用したデータの取得
        List<Diary> diarylist = diarydao.findAll();

        // Viewに渡す
        model.addAttribute("count", diarylist.size());
        model.addAttribute("stafflist", diarylist);

        return "index";
    }

    // 【JPAでDELETE】
    @RequestMapping("/del/{id}")
    public String destory(@PathVariable Long id) {
        diarydao.deleteById(id);

        return "redirect:/";
    }

    // 【JPAでUPDATE1：編集画面の表示(SELECT)】
    @RequestMapping("/edit/{id}")
    public String editView(@PathVariable Long id, Model model) {
        // 値がnullの場合も許可するOptionalで取得
        Optional<Diary> opt1 = diarydao.findById(id);
        Diary s1 = opt1.get();
        model.addAttribute("oneDiary", s1);
        return "edit";
    }

    // 【JPAでUPDATE2：更新処理(UPDATE)】
    @RequestMapping("/edit/{id}/exe")
    public String editExe(@PathVariable Long id, Form form, Model model) {

        // フォームの値をエンティティに入れ直し
        Diary s1 = new Diary();
        s1.setTitle(form.getTitle());
        s1.setContent(form.getContent());

        // 更新の実行(name、idの順)
        // jpadao.updateDb(s1.getName(),s1.getComment(),id);
        diarydao.save(s1);

        // 一覧画面へリダイレクト
        return "redirect:/";
    }

    @RequestMapping("send")
    public String send(Form form, Model model) {
        diarydao.insertDb(form.getTitle(), form.getContent());
        return "redirect:/";
    }
}
