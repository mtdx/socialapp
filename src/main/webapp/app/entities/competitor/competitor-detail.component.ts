import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { JhiEventManager  } from 'ng-jhipster';

import { Competitor } from './competitor.model';
import { CompetitorService } from './competitor.service';

@Component({
    selector: 'jhi-competitor-detail',
    templateUrl: './competitor-detail.component.html'
})
export class CompetitorDetailComponent implements OnInit, OnDestroy {

    competitor: Competitor;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private competitorService: CompetitorService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInCompetitors();
    }

    load(id) {
        this.competitorService.find(id).subscribe((competitor) => {
            this.competitor = competitor;
        });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInCompetitors() {
        this.eventSubscriber = this.eventManager.subscribe(
            'competitorListModification',
            (response) => this.load(this.competitor.id)
        );
    }
}
