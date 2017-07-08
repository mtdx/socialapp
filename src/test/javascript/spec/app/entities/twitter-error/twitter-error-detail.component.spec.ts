/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async, inject } from '@angular/core/testing';
import { OnInit } from '@angular/core';
import { DatePipe } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs/Rx';
import { JhiDateUtils, JhiDataUtils, JhiEventManager } from 'ng-jhipster';
import { SocialappTestModule } from '../../../test.module';
import { MockActivatedRoute } from '../../../helpers/mock-route.service';
import { TwitterErrorDetailComponent } from '../../../../../../main/webapp/app/entities/twitter-error/twitter-error-detail.component';
import { TwitterErrorService } from '../../../../../../main/webapp/app/entities/twitter-error/twitter-error.service';
import { TwitterError } from '../../../../../../main/webapp/app/entities/twitter-error/twitter-error.model';

describe('Component Tests', () => {

    describe('TwitterError Management Detail Component', () => {
        let comp: TwitterErrorDetailComponent;
        let fixture: ComponentFixture<TwitterErrorDetailComponent>;
        let service: TwitterErrorService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [SocialappTestModule],
                declarations: [TwitterErrorDetailComponent],
                providers: [
                    JhiDateUtils,
                    JhiDataUtils,
                    DatePipe,
                    {
                        provide: ActivatedRoute,
                        useValue: new MockActivatedRoute({id: 123})
                    },
                    TwitterErrorService,
                    JhiEventManager
                ]
            }).overrideTemplate(TwitterErrorDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(TwitterErrorDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(TwitterErrorService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
            // GIVEN

            spyOn(service, 'find').and.returnValue(Observable.of(new TwitterError(10)));

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.find).toHaveBeenCalledWith(123);
            expect(comp.twitterError).toEqual(jasmine.objectContaining({id: 10}));
            });
        });
    });

});
