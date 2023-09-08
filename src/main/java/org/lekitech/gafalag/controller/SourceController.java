package org.lekitech.gafalag.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.lekitech.gafalag.entity.v1.Source;
import org.lekitech.gafalag.service.SourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping(path = "/source")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class SourceController {

    private final SourceService sourceService;

    @GetMapping(path = "")
    public List<Source> getAllSources() {
        return sourceService.getAll();
    }

    @GetMapping("/{sourceId}")
    public Source getSourceById(@RequestParam UUID sourceId) {
        return sourceService.getById(sourceId);
    }
}
