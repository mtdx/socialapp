/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async, inject } from '@angular/core/testing';
import { OnInit } from '@angular/core';
import { DatePipe } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs/Rx';
import { JhiDateUtils, JhiDataUtils, JhiEventManager } from 'ng-jhipster';
import { SocialappTestModule } from '../../../test.module';
import { MockActivatedRoute } from '../../../helpers/mock-route.service';
import { CompetitorDetailComponent } from '../../../../../../main/webapp/app/entities/competitor/competitor-detail.component';
import { CompetitorService } from '../../../../../../main/webapp/app/entities/competitor/competitor.service';
import { Competitor } from '../../../../../../main/webapp/app/entities/competitor/competitor.model';

describe('Component Tests', () => {

    describe('Competitor Management Detail Component', () => {
        let comp: CompetitorDetailComponent;
        let fixture: ComponentFixture<CompetitorDetailComponent>;
        let service: CompetitorService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [SocialappTestModule],
                declarations: [CompetitorDetailComponent],
                providers: [
                    JhiDateUtils,
                    JhiDataUtils,
                    DatePipe,
                    {
                        provide: ActivatedRoute,
                        useValue: new MockActivatedRoute({id: 123})
                    },
                    CompetitorService,
                    JhiEventManager
                ]
            }).overrideTemplate(CompetitorDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(CompetitorDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(CompetitorService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
            // GIVEN

            spyOn(service, 'find').and.returnValue(Observable.of(new Competitor(10)));

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.find).toHaveBeenCalledWith(123);
            expect(comp.competitor).toEqual(jasmine.objectContaining({id: 10}));
            });
        });
    });

});
