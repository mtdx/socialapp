import { Injectable, Component } from '@angular/core';
import { Router } from '@angular/router';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { DatePipe } from '@angular/common';
import { TwitterKeyword } from './twitter-keyword.model';
import { TwitterKeywordService } from './twitter-keyword.service';

@Injectable()
export class TwitterKeywordPopupService {
    private isOpen = false;
    constructor(
        private datePipe: DatePipe,
        private modalService: NgbModal,
        private router: Router,
        private twitterKeywordService: TwitterKeywordService

    ) {}

    open(component: Component, id?: number | any): NgbModalRef {
        if (this.isOpen) {
            return;
        }
        this.isOpen = true;

        if (id) {
            this.twitterKeywordService.find(id).subscribe((twitterKeyword) => {
                twitterKeyword.created = this.datePipe
                    .transform(twitterKeyword.created, 'yyyy-MM-ddThh:mm');
                this.twitterKeywordModalRef(component, twitterKeyword);
            });
        } else {
            return this.twitterKeywordModalRef(component, new TwitterKeyword());
        }
    }

    twitterKeywordModalRef(component: Component, twitterKeyword: TwitterKeyword): NgbModalRef {
        const modalRef = this.modalService.open(component, { size: 'lg', backdrop: 'static'});
        modalRef.componentInstance.twitterKeyword = twitterKeyword;
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
