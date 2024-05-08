package com.effortless.effortlessmarket.domain.file;

import com.effortless.effortlessmarket.global.annotaion.ExcludeLogging;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/file")
public class FileController {

    @ExcludeLogging
    @GetMapping
    public ResponseEntity<Resource> getFile(@ModelAttribute("filepath") String filepath) throws FileNotFoundException {
        InputStreamResource resource = new InputStreamResource(new FileInputStream(filepath));
        return ResponseEntity.ok().contentType(MediaType.parseMediaType("application/octet-stream")).body(resource);
    }
}
