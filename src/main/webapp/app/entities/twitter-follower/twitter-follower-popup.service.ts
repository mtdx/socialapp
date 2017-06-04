import { Injectable, Component } from '@angular/core';
import { Router } from '@angular/router';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { DatePipe } from '@angular/common';
import { TwitterFollower } from './twitter-follower.model';
import { TwitterFollowerService } from './twitter-follower.service';
@Injectable()
export class TwitterFollowerPopupService {
    private isOpen = false;
    constructor(
        private datePipe: DatePipe,
        private modalService: NgbModal,
        private router: Router,
        private twitterFollowerService: TwitterFollowerService

    ) {}

    open(component: Component, id?: number | any): NgbModalRef {
        if (this.isOpen) {
            return;
        }
        this.isOpen = true;

        if (id) {
            this.twitterFollowerService.find(id).subscribe((twitterFollower) => {
                twitterFollower.updated = this.datePipe
                    .transform(twitterFollower.updated, 'yyyy-MM-ddThh:mm');
                this.twitterFollowerModalRef(component, twitterFollower);
            });
        } else {
            return this.twitterFollowerModalRef(component, new TwitterFollower());
        }
    }

    twitterFollowerModalRef(component: Component, twitterFollower: TwitterFollower): NgbModalRef {
        const modalRef = this.modalService.open(component, { size: 'lg', backdrop: 'static'});
        modalRef.componentInstance.twitterFollower = twitterFollower;
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
