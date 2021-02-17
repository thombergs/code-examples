package io.jgoerner.s3.adapter.in;

import io.jgoerner.s3.domain.ObjectPartial;
import io.jgoerner.s3.domain.SpacePartial;
import lombok.Data;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Map;
import java.util.Optional;

@Log4j2
@Controller()
public class View {

  private final RestApi api;

  public View(RestApi api) {
    this.api = api;
  }

  @GetMapping("space")
  String space(Model model) {
    var spaces = api.getSpaces();
    model.addAttribute("spaces", spaces);
    return "space-overview";
  }

  @GetMapping("space/{name}")
  String spaceDetail(@PathVariable String name, Model model) {
    model.addAllAttributes(Map.of("space", name, "objects", api.getObjectsInSpace(name)));
    return "space-detail";
  }

  @GetMapping("space/{space}/make-public/{key}")
  String makePublic(
      @PathVariable String space, @PathVariable String key, RedirectAttributes redirectAttributes) {
    api.patchObject(space, key, new ObjectPartial(true));
    redirectAttributes.addFlashAttribute("success", "Made object public");
    return "redirect:/space/" + space;
  }

  @GetMapping("space/{space}/make-private/{key}")
  String makePrivate(
      @PathVariable String space, @PathVariable String key, RedirectAttributes redirectAttributes) {
    api.patchObject(space, key, new ObjectPartial(false));
    redirectAttributes.addFlashAttribute("success", "Made object private");
    return "redirect:/space/" + space;
  }

  @GetMapping("space/{space}/delete/{key}")
  String deleteObject(@PathVariable String space, @PathVariable String key) {
    api.deleteObject(space, key);
    return "redirect:/space/" + space;
  }

  @GetMapping("/space/{space}/object-form")
  String objectNew(@PathVariable String space, Model model) {
    model.addAllAttributes(Map.of("space", space, "object", new ObjectForm()));
    return "object-form";
  }

  @PostMapping("/space/{space}/object-form")
  String postObject(
      @PathVariable String space,
      @RequestParam("file") MultipartFile file,
      @ModelAttribute ObjectForm form,
      RedirectAttributes redirectAttributes) {
    api.postObject(space, file, form.getName());
    redirectAttributes.addFlashAttribute("success", "Successfully uploaded " + form.getName());
    return "redirect:/space/" + space;
  }

  @GetMapping("/space/{space}/magic/{key}")
  String magicLink(
      @PathVariable String space, @PathVariable String key, RedirectAttributes redirectAttributes) {
    var link = api.createLink(space, key, 15L);
    redirectAttributes.addFlashAttribute("warn", link);
    return "redirect:/space/" + space;
  }

  @GetMapping("space-form")
  String spaceNew(Model model) {
    model.addAttribute("space", new SpaceForm());
    return "space-form";
  }

  @PostMapping("space-form")
  String newSpace(@ModelAttribute SpaceForm form, RedirectAttributes redirectAttributes) {
    api.postSpace(form.getName());
    redirectAttributes.addFlashAttribute("success", "Successfully created " + form.getName());
    return "redirect:space";
  }

  @GetMapping("space/temporary/{name}")
  String makeTemporary(@PathVariable String name) {
    api.patchSpace(name, new SpacePartial(2));
    return "redirect:/space";
  }

  @GetMapping("space/permanent/{name}")
  String makePermanent(@PathVariable String name) {
    api.patchSpace(name, new SpacePartial(-1));
    return "redirect:/space";
  }

  @GetMapping("space/delete/{name}")
  String delete(@PathVariable String name) {
    api.deleteSpace(name, Optional.of(true));
    return "redirect:/space";
  }
}

@Data
class SpaceForm {
  String name;
}

@Data
class ObjectForm {
  String name;
}
