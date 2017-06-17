import { Injectable, Component } from '@angular/core';
import { Router } from '@angular/router';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { TwitterError } from './twitter-error.model';
import { TwitterErrorService } from './twitter-error.service';

@Injectable()
export class TwitterErrorPopupService {
    private isOpen = false;
    constructor(
        private modalService: NgbModal,
        private router: Router,
        private twitterErrorService: TwitterErrorService

    ) {}

    open(component: Component, id?: number | any): NgbModalRef {
        if (this.isOpen) {
            return;
        }
        this.isOpen = true;

        if (id) {
            this.twitterErrorService.find(id).subscribe((twitterError) => {
                this.twitterErrorModalRef(component, twitterError);
            });
        } else {
            return this.twitterErrorModalRef(component, new TwitterError());
        }
    }

    twitterErrorModalRef(component: Component, twitterError: TwitterError): NgbModalRef {
        const modalRef = this.modalService.open(component, { size: 'lg', backdrop: 'static'});
        modalRef.componentInstance.twitterError = twitterError;
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
