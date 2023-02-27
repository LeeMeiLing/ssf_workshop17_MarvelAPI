package sg.edu.nus.iss.ssf_workshop17_marvel.controllers;

import java.security.NoSuchAlgorithmException;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import sg.edu.nus.iss.ssf_workshop17_marvel.services.CharacterService;

@Controller
@RequestMapping(path={"/","/index"})
public class CharacterController {

    @Autowired
    private CharacterService charSvc;
    
    @GetMapping
    public String Home (Model model){

        Map<Integer,String> charMap = charSvc.getCharactersMap();
        model.addAttribute("charMap", charMap);
        return "index";

    }


    @GetMapping("/characters/characterId")
    public String displayCharacter(@RequestParam String selected, Model model) throws NoSuchAlgorithmException{

        Integer charSelected = Integer.parseInt(selected);

        if (charSelected == 0){
            return "index";
        }
        else{
            model.addAttribute("character", charSvc.getCharacter(charSelected));
            return "character";
        }
        
    }

}
