package drg.mentalhealth.support.controller;

import drg.mentalhealth.support.model.MentalHealthResource;
import drg.mentalhealth.support.model.ResourceType;
import drg.mentalhealth.support.repository.MentalHealthResourceRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/resources")
public class MentalHealthResourceController {
    @Autowired
    private MentalHealthResourceRepository resourceRepository;


    @PostMapping("/upload")
    public ResponseEntity<?> uploadResource(
            @RequestBody MentalHealthResource resource
    ) {

        if (resource.getResourceType().equals(ResourceType.ARTICLE)){
            resource.setResourceType(ResourceType.ARTICLE);
        }
        else if (resource.getResourceType().equals(ResourceType.EXERCISE)){
            resource.setResourceType(ResourceType.EXERCISE);
        }
        else if (resource.getResourceType().equals(ResourceType.GUIDE)){
            resource.setResourceType(ResourceType.GUIDE);
        }
        else {
            return ResponseEntity.badRequest().body("{\"error\": \"Invalid resource type\"}");
        }

        MentalHealthResource savedResource = resourceRepository.save(resource);
        return ResponseEntity.ok(savedResource);
    }
    @GetMapping
    public ResponseEntity<List<MentalHealthResource>> listResources(
            @RequestParam(required = false) List<Long> ids
    ) {
        List<MentalHealthResource> resources;
        if (ids != null && !ids.isEmpty()) {
            resources = resourceRepository.findAllById(ids);
        } else {
            resources = resourceRepository.findAll();
        }
        return ResponseEntity.ok(resources);
    }
}
