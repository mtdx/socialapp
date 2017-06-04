import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { EventManager  } from 'ng-jhipster';

import { TwitterFollower } from './twitter-follower.model';
import { TwitterFollowerService } from './twitter-follower.service';

@Component({
    selector: 'jhi-twitter-follower-detail',
    templateUrl: './twitter-follower-detail.component.html'
})
export class TwitterFollowerDetailComponent implements OnInit, OnDestroy {

    twitterFollower: TwitterFollower;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: EventManager,
        private twitterFollowerService: TwitterFollowerService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInTwitterFollowers();
    }

    load(id) {
        this.twitterFollowerService.find(id).subscribe((twitterFollower) => {
            this.twitterFollower = twitterFollower;
        });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInTwitterFollowers() {
        this.eventSubscriber = this.eventManager.subscribe(
            'twitterFollowerListModification',
            (response) => this.load(this.twitterFollower.id)
        );
    }
}
