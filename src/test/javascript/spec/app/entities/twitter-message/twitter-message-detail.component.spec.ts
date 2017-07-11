/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async, inject } from '@angular/core/testing';
import { OnInit } from '@angular/core';
import { DatePipe } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs/Rx';
import { JhiDateUtils, JhiDataUtils, JhiEventManager } from 'ng-jhipster';
import { SocialappTestModule } from '../../../test.module';
import { MockActivatedRoute } from '../../../helpers/mock-route.service';
import { TwitterMessageDetailComponent } from '../../../../../../main/webapp/app/entities/twitter-message/twitter-message-detail.component';
import { TwitterMessageService } from '../../../../../../main/webapp/app/entities/twitter-message/twitter-message.service';
import { TwitterMessage } from '../../../../../../main/webapp/app/entities/twitter-message/twitter-message.model';

describe('Component Tests', () => {

    describe('TwitterMessage Management Detail Component', () => {
        let comp: TwitterMessageDetailComponent;
        let fixture: ComponentFixture<TwitterMessageDetailComponent>;
        let service: TwitterMessageService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [SocialappTestModule],
                declarations: [TwitterMessageDetailComponent],
                providers: [
                    JhiDateUtils,
                    JhiDataUtils,
                    DatePipe,
                    {
                        provide: ActivatedRoute,
                        useValue: new MockActivatedRoute({id: 123})
                    },
                    TwitterMessageService,
                    JhiEventManager
                ]
            }).overrideTemplate(TwitterMessageDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(TwitterMessageDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(TwitterMessageService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
            // GIVEN

            spyOn(service, 'find').and.returnValue(Observable.of(new TwitterMessage(10)));

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.find).toHaveBeenCalledWith(123);
            expect(comp.twitterMessage).toEqual(jasmine.objectContaining({id: 10}));
            });
        });
    });

});
