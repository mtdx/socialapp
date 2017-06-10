import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { EventManager  } from 'ng-jhipster';

import { Proxy } from './proxy.model';
import { ProxyService } from './proxy.service';

@Component({
    selector: 'jhi-proxy-detail',
    templateUrl: './proxy-detail.component.html'
})
export class ProxyDetailComponent implements OnInit, OnDestroy {

    proxy: Proxy;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: EventManager,
        private proxyService: ProxyService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInProxies();
    }

    load(id) {
        this.proxyService.find(id).subscribe((proxy) => {
            this.proxy = proxy;
        });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInProxies() {
        this.eventSubscriber = this.eventManager.subscribe(
            'proxyListModification',
            (response) => this.load(this.proxy.id)
        );
    }
}
