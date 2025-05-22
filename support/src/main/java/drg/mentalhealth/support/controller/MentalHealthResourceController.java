package drg.mentalhealth.support.controller;

import drg.mentalhealth.support.model.MentalHealthResource;
import drg.mentalhealth.support.model.ResourceType;
import drg.mentalhealth.support.repository.MentalHealthResourceRepository;
import drg.mentalhealth.support.service.MentalHealthResourceService;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/resources")
public class MentalHealthResourceController {
    @Autowired
    private MentalHealthResourceService resourceService;


    @PostMapping("/upload")
    public ResponseEntity<?> uploadResource(
            @RequestBody MentalHealthResource resource
    ) {
        MentalHealthResource savedResource = resourceService.saveResource(resource);
        if (savedResource == null) {
            return ResponseEntity.status(400).body("Failed to save resource");
        }
        return ResponseEntity.ok(savedResource);
    }

    
    @GetMapping
    public ResponseEntity<List<MentalHealthResource>> listResources(
            @RequestParam(required = false) List<Long> ids
    ) {
        List<MentalHealthResource> resources = resourceService.getAllResources();
        if (ids != null && !ids.isEmpty()) {
            resources = resources.stream()
                    .filter(resource -> ids.contains(resource.getId()))
                    .toList();
        }
        
        return ResponseEntity.ok(resources);
    }
}
