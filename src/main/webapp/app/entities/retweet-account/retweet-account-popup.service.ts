import { Injectable, Component } from '@angular/core';
import { Router } from '@angular/router';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { DatePipe } from '@angular/common';
import { RetweetAccount } from './retweet-account.model';
import { RetweetAccountService } from './retweet-account.service';

@Injectable()
export class RetweetAccountPopupService {
    private isOpen = false;
    constructor(
        private datePipe: DatePipe,
        private modalService: NgbModal,
        private router: Router,
        private retweetAccountService: RetweetAccountService

    ) {}

    open(component: Component, id?: number | any): NgbModalRef {
        if (this.isOpen) {
            return;
        }
        this.isOpen = true;

        if (id) {
            this.retweetAccountService.find(id).subscribe((retweetAccount) => {
                retweetAccount.created = this.datePipe
                    .transform(retweetAccount.created, 'yyyy-MM-ddThh:mm');
                this.retweetAccountModalRef(component, retweetAccount);
            });
        } else {
            return this.retweetAccountModalRef(component, new RetweetAccount());
        }
    }

    retweetAccountModalRef(component: Component, retweetAccount: RetweetAccount): NgbModalRef {
        const modalRef = this.modalService.open(component, { size: 'lg', backdrop: 'static'});
        modalRef.componentInstance.retweetAccount = retweetAccount;
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
