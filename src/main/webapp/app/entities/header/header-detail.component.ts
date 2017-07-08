import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { JhiEventManager , JhiDataUtils } from 'ng-jhipster';

import { Header } from './header.model';
import { HeaderService } from './header.service';

@Component({
    selector: 'jhi-header-detail',
    templateUrl: './header-detail.component.html'
})
export class HeaderDetailComponent implements OnInit, OnDestroy {

    header: Header;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private dataUtils: JhiDataUtils,
        private headerService: HeaderService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInHeaders();
    }

    load(id) {
        this.headerService.find(id).subscribe((header) => {
            this.header = header;
        });
    }
    byteSize(field) {
        return this.dataUtils.byteSize(field);
    }

    openFile(contentType, field) {
        return this.dataUtils.openFile(contentType, field);
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInHeaders() {
        this.eventSubscriber = this.eventManager.subscribe(
            'headerListModification',
            (response) => this.load(this.header.id)
        );
    }
}
