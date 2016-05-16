<?php

namespace AppBundle\Controller;

use AppBundle\Entity\Approval;
use Sensio\Bundle\FrameworkExtraBundle\Configuration\Route;
use Symfony\Bundle\FrameworkBundle\Controller\Controller;
use Symfony\Component\Form\Extension\Core\Type\ChoiceType;
use Symfony\Component\HttpFoundation\Request;
use GuzzleHttp;

/**
 * @Route("/approval")
 */
class ApprovalController extends Controller
{
    private $client;

    public function __construct()
    {
        $this->client = new GuzzleHttp\Client([
            'base_uri' => 'http://1.appmanager-1311.appspot.com/approval/'
        ]);
    }

    private function getAddForm($approval)
    {
        return $this->createFormBuilder($approval)
            ->add('response', ChoiceType::class, array(
                'choices'  => array(
                    'accepted' => "accepted",
                    'refused' => "refused",
                    'pending' => "pending"
                )
            ))
            ->getForm();
    }

    /**
     * @Route("", name="get_approval")
     */
    public function getApprovalAction(Request $request)
    {
        $response = $this->client->request('GET');

        if ($response->getStatusCode() == 200 && $response->getBody()) {
            $data = json_decode($response->getBody(), true);
        }
        else {
            $this->get('session')->getFlashBag()->add('error',
                'An error occured while retrieving approvals.'
            );
        }

        return $this->render('approval/approval.html.twig', array(
            'data' => $data ?? null
        ));
    }

    /**
     * @Route("/{id}/edit", name="edit_approval")
     */
    public function editAction($id, Request $request)
    {
        $response = $this->client->request('GET', $id);

        $approval = new Approval();
        $approval->loadApiJson($response->getBody());

        $form = $this->getAddForm($approval);

        $form->handleRequest($request);

        if ($form->isSubmitted() && $form->isValid()) {
            $response = $this->client->request('PUT', $id, [
                'json' => $approval->getApiArray()
            ]);

            if ($response->getStatusCode() == 204) {
                $this->get('session')->getFlashBag()->add('success',
                    'Approval has been modified.'
                );
            }
            else {
                $this->get('session')->getFlashBag()->add('error',
                    'Approval has not been modified.'
                );
            }

            return $this->redirectToRoute('get_approval');
        }

        return $this->render('approval/edit.html.twig', array(
            'form' => $form->createView()
        ));
    }
}
