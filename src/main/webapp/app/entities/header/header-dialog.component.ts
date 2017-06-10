import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { Observable } from 'rxjs/Rx';
import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { EventManager, AlertService, DataUtils } from 'ng-jhipster';

import { Header } from './header.model';
import { HeaderPopupService } from './header-popup.service';
import { HeaderService } from './header.service';

@Component({
    selector: 'jhi-header-dialog',
    templateUrl: './header-dialog.component.html'
})
export class HeaderDialogComponent implements OnInit {

    header: Header;
    authorities: any[];
    isSaving: boolean;

    constructor(
        public activeModal: NgbActiveModal,
        private dataUtils: DataUtils,
        private alertService: AlertService,
        private headerService: HeaderService,
        private eventManager: EventManager
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
        this.authorities = ['ROLE_USER', 'ROLE_ADMIN'];
    }
    byteSize(field) {
        return this.dataUtils.byteSize(field);
    }

    openFile(contentType, field) {
        return this.dataUtils.openFile(contentType, field);
    }

    setFileData(event, header, field, isImage) {
        if (event.target.files && event.target.files[0]) {
            const file = event.target.files[0];
            if (isImage && !/^image\//.test(file.type)) {
                return;
            }
            this.dataUtils.toBase64(file, (base64Data) => {
                header[field] = base64Data;
                header[`${field}ContentType`] = file.type;
            });
        }
    }
    clear() {
        this.activeModal.dismiss('cancel');
    }

    save() {
        this.isSaving = true;
        if (this.header.id !== undefined) {
            this.subscribeToSaveResponse(
                this.headerService.update(this.header), false);
        } else {
            this.subscribeToSaveResponse(
                this.headerService.create(this.header), true);
        }
    }

    private subscribeToSaveResponse(result: Observable<Header>, isCreated: boolean) {
        result.subscribe((res: Header) =>
            this.onSaveSuccess(res, isCreated), (res: Response) => this.onSaveError(res));
    }

    private onSaveSuccess(result: Header, isCreated: boolean) {
        this.alertService.success(
            isCreated ? 'socialappApp.header.created'
            : 'socialappApp.header.updated',
            { param : result.id }, null);

        this.eventManager.broadcast({ name: 'headerListModification', content: 'OK'});
        this.isSaving = false;
        this.activeModal.dismiss(result);
    }

    private onSaveError(error) {
        try {
            error.json();
        } catch (exception) {
            error.message = error.text();
        }
        this.isSaving = false;
        this.onError(error);
    }

    private onError(error) {
        this.alertService.error(error.message, null, null);
    }
}

@Component({
    selector: 'jhi-header-popup',
    template: ''
})
export class HeaderPopupComponent implements OnInit, OnDestroy {

    modalRef: NgbModalRef;
    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private headerPopupService: HeaderPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.modalRef = this.headerPopupService
                    .open(HeaderDialogComponent, params['id']);
            } else {
                this.modalRef = this.headerPopupService
                    .open(HeaderDialogComponent);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
