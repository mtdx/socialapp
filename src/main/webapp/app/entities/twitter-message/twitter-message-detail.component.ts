import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { JhiEventManager  } from 'ng-jhipster';

import { TwitterMessage } from './twitter-message.model';
import { TwitterMessageService } from './twitter-message.service';

@Component({
    selector: 'jhi-twitter-message-detail',
    templateUrl: './twitter-message-detail.component.html'
})
export class TwitterMessageDetailComponent implements OnInit, OnDestroy {

    twitterMessage: TwitterMessage;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private twitterMessageService: TwitterMessageService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInTwitterMessages();
    }

    load(id) {
        this.twitterMessageService.find(id).subscribe((twitterMessage) => {
            this.twitterMessage = twitterMessage;
        });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInTwitterMessages() {
        this.eventSubscriber = this.eventManager.subscribe(
            'twitterMessageListModification',
            (response) => this.load(this.twitterMessage.id)
        );
    }
}
