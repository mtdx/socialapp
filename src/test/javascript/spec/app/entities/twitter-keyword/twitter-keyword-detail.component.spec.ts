/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async, inject } from '@angular/core/testing';
import { OnInit } from '@angular/core';
import { DatePipe } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs/Rx';
import { JhiDateUtils, JhiDataUtils, JhiEventManager } from 'ng-jhipster';
import { SocialappTestModule } from '../../../test.module';
import { MockActivatedRoute } from '../../../helpers/mock-route.service';
import { TwitterKeywordDetailComponent } from '../../../../../../main/webapp/app/entities/twitter-keyword/twitter-keyword-detail.component';
import { TwitterKeywordService } from '../../../../../../main/webapp/app/entities/twitter-keyword/twitter-keyword.service';
import { TwitterKeyword } from '../../../../../../main/webapp/app/entities/twitter-keyword/twitter-keyword.model';

describe('Component Tests', () => {

    describe('TwitterKeyword Management Detail Component', () => {
        let comp: TwitterKeywordDetailComponent;
        let fixture: ComponentFixture<TwitterKeywordDetailComponent>;
        let service: TwitterKeywordService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [SocialappTestModule],
                declarations: [TwitterKeywordDetailComponent],
                providers: [
                    JhiDateUtils,
                    JhiDataUtils,
                    DatePipe,
                    {
                        provide: ActivatedRoute,
                        useValue: new MockActivatedRoute({id: 123})
                    },
                    TwitterKeywordService,
                    JhiEventManager
                ]
            }).overrideTemplate(TwitterKeywordDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(TwitterKeywordDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(TwitterKeywordService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
            // GIVEN

            spyOn(service, 'find').and.returnValue(Observable.of(new TwitterKeyword(10)));

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.find).toHaveBeenCalledWith(123);
            expect(comp.twitterKeyword).toEqual(jasmine.objectContaining({id: 10}));
            });
        });
    });

});
