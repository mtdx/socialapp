import { Injectable, Component } from '@angular/core';
import { Router } from '@angular/router';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { DatePipe } from '@angular/common';
import { Competitor } from './competitor.model';
import { CompetitorService } from './competitor.service';

@Injectable()
export class CompetitorPopupService {
    private isOpen = false;
    constructor(
        private datePipe: DatePipe,
        private modalService: NgbModal,
        private router: Router,
        private competitorService: CompetitorService

    ) {}

    open(component: Component, id?: number | any): NgbModalRef {
        if (this.isOpen) {
            return;
        }
        this.isOpen = true;

        if (id) {
            this.competitorService.find(id).subscribe((competitor) => {
                competitor.created = this.datePipe
                    .transform(competitor.created, 'yyyy-MM-ddThh:mm');
                this.competitorModalRef(component, competitor);
            });
        } else {
            return this.competitorModalRef(component, new Competitor());
        }
    }

    competitorModalRef(component: Component, competitor: Competitor): NgbModalRef {
        const modalRef = this.modalService.open(component, { size: 'lg', backdrop: 'static'});
        modalRef.componentInstance.competitor = competitor;
        modalRef.result.then((result) => {
            this.router.navigate([{ outlets: { popup: null }}], { replaceUrl: true });
            this.isOpen = false;
        }, (reason) => {
            this.router.navigate([{ outlets: { popup: null }}], { replaceUrl: true });
            this.isOpen = false;
        });
        return modalRef;
    }
}
