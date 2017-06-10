package com.ninja.socialapp.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.ninja.socialapp.domain.Avatar;
import com.ninja.socialapp.service.AvatarService;
import com.ninja.socialapp.web.rest.util.HeaderUtil;
import com.ninja.socialapp.web.rest.util.PaginationUtil;
import io.swagger.annotations.ApiParam;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing Avatar.
 */
@RestController
@RequestMapping("/api")
public class AvatarResource {

    private final Logger log = LoggerFactory.getLogger(AvatarResource.class);

    private static final String ENTITY_NAME = "avatar";

    private final AvatarService avatarService;

    public AvatarResource(AvatarService avatarService) {
        this.avatarService = avatarService;
    }

    /**
     * POST  /avatars : Create a new avatar.
     *
     * @param avatar the avatar to create
     * @return the ResponseEntity with status 201 (Created) and with body the new avatar, or with status 400 (Bad Request) if the avatar has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/avatars")
    @Timed
    public ResponseEntity<Avatar> createAvatar(@Valid @RequestBody Avatar avatar) throws URISyntaxException {
        log.debug("REST request to save Avatar : {}", avatar);
        if (avatar.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new avatar cannot already have an ID")).body(null);
        }
        Avatar result = avatarService.save(avatar);
        return ResponseEntity.created(new URI("/api/avatars/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /avatars : Updates an existing avatar.
     *
     * @param avatar the avatar to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated avatar,
     * or with status 400 (Bad Request) if the avatar is not valid,
     * or with status 500 (Internal Server Error) if the avatar couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/avatars")
    @Timed
    public ResponseEntity<Avatar> updateAvatar(@Valid @RequestBody Avatar avatar) throws URISyntaxException {
        log.debug("REST request to update Avatar : {}", avatar);
        if (avatar.getId() == null) {
            return createAvatar(avatar);
        }
        Avatar result = avatarService.save(avatar);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, avatar.getId().toString()))
            .body(result);
    }

    /**
     * GET  /avatars : get all the avatars.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of avatars in body
     */
    @GetMapping("/avatars")
    @Timed
    public ResponseEntity<List<Avatar>> getAllAvatars(@ApiParam Pageable pageable) {
        log.debug("REST request to get a page of Avatars");
        Page<Avatar> page = avatarService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/avatars");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /avatars/:id : get the "id" avatar.
     *
     * @param id the id of the avatar to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the avatar, or with status 404 (Not Found)
     */
    @GetMapping("/avatars/{id}")
    @Timed
    public ResponseEntity<Avatar> getAvatar(@PathVariable Long id) {
        log.debug("REST request to get Avatar : {}", id);
        Avatar avatar = avatarService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(avatar));
    }

    /**
     * DELETE  /avatars/:id : delete the "id" avatar.
     *
     * @param id the id of the avatar to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/avatars/{id}")
    @Timed
    public ResponseEntity<Void> deleteAvatar(@PathVariable Long id) {
        log.debug("REST request to delete Avatar : {}", id);
        avatarService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/avatars?query=:query : search for the avatar corresponding
     * to the query.
     *
     * @param query the query of the avatar search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/avatars")
    @Timed
    public ResponseEntity<List<Avatar>> searchAvatars(@RequestParam String query, @ApiParam Pageable pageable) {
        log.debug("REST request to search for a page of Avatars for query {}", query);
        Page<Avatar> page = avatarService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/avatars");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
