import { Injectable, Component } from '@angular/core';
import { Router } from '@angular/router';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { TwitterAccount } from './twitter-account.model';
import { TwitterAccountService } from './twitter-account.service';
@Injectable()
export class TwitterAccountPopupService {
    private isOpen = false;
    constructor(
        private modalService: NgbModal,
        private router: Router,
        private twitterAccountService: TwitterAccountService

    ) {}

    open(component: Component, id?: number | any): NgbModalRef {
        if (this.isOpen) {
            return;
        }
        this.isOpen = true;

        if (id) {
            this.twitterAccountService.find(id).subscribe((twitterAccount) => {
                this.twitterAccountModalRef(component, twitterAccount);
            });
        } else {
            return this.twitterAccountModalRef(component, new TwitterAccount());
        }
    }

    twitterAccountModalRef(component: Component, twitterAccount: TwitterAccount): NgbModalRef {
        const modalRef = this.modalService.open(component, { size: 'lg', backdrop: 'static'});
        modalRef.componentInstance.twitterAccount = twitterAccount;
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
