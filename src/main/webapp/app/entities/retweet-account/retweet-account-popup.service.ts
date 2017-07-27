import { Injectable, Component } from '@angular/core';
import { Router } from '@angular/router';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { DatePipe } from '@angular/common';
import { RetweetAccount } from './retweet-account.model';
import { RetweetAccountService } from './retweet-account.service';

@Injectable()
export class RetweetAccountPopupService {
    private ngbModalRef: NgbModalRef;

    constructor(
        private datePipe: DatePipe,
        private modalService: NgbModal,
        private router: Router,
        private retweetAccountService: RetweetAccountService

    ) {
        this.ngbModalRef = null;
    }

    open(component: Component, id?: number | any): Promise<NgbModalRef> {
        return new Promise<NgbModalRef>((resolve, reject) => {
            const isOpen = this.ngbModalRef !== null;
            if (isOpen) {
                resolve(this.ngbModalRef);
            }

            if (id) {
                this.retweetAccountService.find(id).subscribe((retweetAccount) => {
                    retweetAccount.created = this.datePipe
                        .transform(retweetAccount.created, 'yyyy-MM-ddThh:mm');
                    this.ngbModalRef = this.retweetAccountModalRef(component, retweetAccount);
                    resolve(this.ngbModalRef);
                });
            } else {
                // setTimeout used as a workaround for getting ExpressionChangedAfterItHasBeenCheckedError
                setTimeout(() => {
                    this.ngbModalRef = this.retweetAccountModalRef(component, new RetweetAccount());
                    resolve(this.ngbModalRef);
                }, 0);
            }
        });
    }

    retweetAccountModalRef(component: Component, retweetAccount: RetweetAccount): NgbModalRef {
        const modalRef = this.modalService.open(component, { size: 'lg', backdrop: 'static'});
        modalRef.componentInstance.retweetAccount = retweetAccount;
        modalRef.result.then((result) => {
            this.router.navigate([{ outlets: { popup: null }}], { replaceUrl: true });
            this.ngbModalRef = null;
        }, (reason) => {
            this.router.navigate([{ outlets: { popup: null }}], { replaceUrl: true });
            this.ngbModalRef = null;
        });
        return modalRef;
    }
}
