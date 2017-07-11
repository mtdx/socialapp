import { Injectable, Component } from '@angular/core';
import { Router } from '@angular/router';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { TwitterMessage } from './twitter-message.model';
import { TwitterMessageService } from './twitter-message.service';

@Injectable()
export class TwitterMessagePopupService {
    private isOpen = false;
    constructor(
        private modalService: NgbModal,
        private router: Router,
        private twitterMessageService: TwitterMessageService

    ) {}

    open(component: Component, id?: number | any): NgbModalRef {
        if (this.isOpen) {
            return;
        }
        this.isOpen = true;

        if (id) {
            this.twitterMessageService.find(id).subscribe((twitterMessage) => {
                this.twitterMessageModalRef(component, twitterMessage);
            });
        } else {
            return this.twitterMessageModalRef(component, new TwitterMessage());
        }
    }

    twitterMessageModalRef(component: Component, twitterMessage: TwitterMessage): NgbModalRef {
        const modalRef = this.modalService.open(component, { size: 'lg', backdrop: 'static'});
        modalRef.componentInstance.twitterMessage = twitterMessage;
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
