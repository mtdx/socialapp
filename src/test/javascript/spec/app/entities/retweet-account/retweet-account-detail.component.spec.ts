/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async, inject } from '@angular/core/testing';
import { OnInit } from '@angular/core';
import { DatePipe } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs/Rx';
import { JhiDateUtils, JhiDataUtils, JhiEventManager } from 'ng-jhipster';
import { SocialappTestModule } from '../../../test.module';
import { MockActivatedRoute } from '../../../helpers/mock-route.service';
import { RetweetAccountDetailComponent } from '../../../../../../main/webapp/app/entities/retweet-account/retweet-account-detail.component';
import { RetweetAccountService } from '../../../../../../main/webapp/app/entities/retweet-account/retweet-account.service';
import { RetweetAccount } from '../../../../../../main/webapp/app/entities/retweet-account/retweet-account.model';

describe('Component Tests', () => {

    describe('RetweetAccount Management Detail Component', () => {
        let comp: RetweetAccountDetailComponent;
        let fixture: ComponentFixture<RetweetAccountDetailComponent>;
        let service: RetweetAccountService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [SocialappTestModule],
                declarations: [RetweetAccountDetailComponent],
                providers: [
                    JhiDateUtils,
                    JhiDataUtils,
                    DatePipe,
                    {
                        provide: ActivatedRoute,
                        useValue: new MockActivatedRoute({id: 123})
                    },
                    RetweetAccountService,
                    JhiEventManager
                ]
            }).overrideTemplate(RetweetAccountDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(RetweetAccountDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(RetweetAccountService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
            // GIVEN

            spyOn(service, 'find').and.returnValue(Observable.of(new RetweetAccount(10)));

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.find).toHaveBeenCalledWith(123);
            expect(comp.retweetAccount).toEqual(jasmine.objectContaining({id: 10}));
            });
        });
    });

});
