import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { JhiEventManager  } from 'ng-jhipster';

import { TwitterKeyword } from './twitter-keyword.model';
import { TwitterKeywordService } from './twitter-keyword.service';

@Component({
    selector: 'jhi-twitter-keyword-detail',
    templateUrl: './twitter-keyword-detail.component.html'
})
export class TwitterKeywordDetailComponent implements OnInit, OnDestroy {

    twitterKeyword: TwitterKeyword;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private twitterKeywordService: TwitterKeywordService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInTwitterKeywords();
    }

    load(id) {
        this.twitterKeywordService.find(id).subscribe((twitterKeyword) => {
            this.twitterKeyword = twitterKeyword;
        });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInTwitterKeywords() {
        this.eventSubscriber = this.eventManager.subscribe(
            'twitterKeywordListModification',
            (response) => this.load(this.twitterKeyword.id)
        );
    }
}
