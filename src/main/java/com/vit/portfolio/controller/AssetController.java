package com.vit.portfolio.controller;

import com.vit.portfolio.model.Asset;
import com.vit.portfolio.model.Alert;
import com.vit.portfolio.service.AssetService;
import com.vit.portfolio.service.AlertService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@Controller
public class AssetController {

    @Autowired
    private AssetService assetService;

    @Autowired
    private AlertService alertService;

    // HOME — redirect to dashboard
    @GetMapping("/")
    public String home() {
        return "redirect:/dashboard";
    }

    // DASHBOARD
    @GetMapping("/dashboard")
    public String dashboard(@RequestParam(required = false) String search, Model model) {
        Map<String, Double> summary = assetService.getSummary();
        model.addAttribute("assets", assetService.searchAssets(search));
        model.addAttribute("summary", summary);
        model.addAttribute("search", search);
        model.addAttribute("triggeredAlerts", alertService.getTriggeredAlerts().size());
        return "dashboard";
    }

    // SHOW ADD FORM
    @GetMapping("/assets/add")
    public String showAddForm(Model model) {
        model.addAttribute("asset", new Asset());
        return "add-asset";
    }

    // SAVE NEW ASSET
    @PostMapping("/assets/add")
    public String saveAsset(@ModelAttribute Asset asset) {
        assetService.saveAsset(asset);
        return "redirect:/dashboard";
    }

    // SHOW EDIT FORM
    @GetMapping("/assets/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        model.addAttribute("asset", assetService.getAssetById(id));
        return "edit-asset";
    }

    // UPDATE ASSET
    @PostMapping("/assets/edit/{id}")
    public String updateAsset(@PathVariable Long id, @ModelAttribute Asset asset) {
        assetService.updateAsset(id, asset);
        return "redirect:/dashboard";
    }

    // DELETE ASSET
    @GetMapping("/assets/delete/{id}")
    public String deleteAsset(@PathVariable Long id) {
        assetService.deleteAsset(id);
        return "redirect:/dashboard";
    }

    // ASSET DETAIL / DRILL-DOWN
    @GetMapping("/assets/{id}")
    public String assetDetail(@PathVariable Long id, Model model) {
        Asset asset = assetService.getAssetById(id);
        model.addAttribute("asset", asset);
        model.addAttribute("alerts", alertService.getAllAlerts()
                .stream()
                .filter(a -> a.getAsset().getId().equals(id))
                .toList());
        return "asset-detail";
    }

    // ALERTS PAGE
    @GetMapping("/alerts")
    public String alertsPage(Model model) {
        model.addAttribute("assets", assetService.getAllAssets());
        model.addAttribute("alerts", alertService.getAllAlerts());
        model.addAttribute("triggered", alertService.getTriggeredAlerts());
        model.addAttribute("newAlert", new Alert());
        return "alerts";
    }

    // SAVE ALERT
    @PostMapping("/alerts/add")
    public String saveAlert(@RequestParam Long assetId,
                            @RequestParam String alertType,
                            @RequestParam Double threshold) {
        Alert alert = new Alert();
        alert.setAsset(assetService.getAssetById(assetId));
        alert.setAlertType(alertType);
        alert.setThreshold(threshold);
        alertService.saveAlert(alert);
        return "redirect:/alerts";
    }

    // DELETE ALERT
    @GetMapping("/alerts/delete/{id}")
    public String deleteAlert(@PathVariable Long id) {
        alertService.deleteAlert(id);
        return "redirect:/alerts";
    }
}