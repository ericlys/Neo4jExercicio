package com.db2.dao;

import com.db2.bens.Usuario;
import com.db2.enumeration.Relacionamentos;
import org.neo4j.graphdb.*;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class userDAO {
    GraphDatabaseService graph = new GraphDatabaseFactory ()
            .newEmbeddedDatabase( new File ("graph.db"));

    private Transaction transaction = graph.beginTx ();

    public Node getNode() {
        Node node = graph.createNode (Label.label ("Usuario"));
        return node;
    }

    public boolean novoUsuario(Usuario usuario){
        Node node = getNode ();
        node.setProperty ("cpf", usuario.getCpf ());
        node.setProperty ("nome", usuario.getNome ());
        node.setProperty ("idade", usuario.getIdade ());
        transaction.success ();
        graph.shutdown ();
        return true;
    }

    public Usuario buscarUsuario(String cpf){
        Node node = getNode ();
        node = graph.findNode (Label.label ("Usuario"), "cpf", cpf);
        transaction.success ();
        graph.shutdown ();
        return (Usuario) node.getAllProperties ();
    }

    public boolean AtualizarUsuario(Usuario usuario, String cpf){
        Node node = getNode ();
        node = (Node) buscarUsuario (cpf);
        node.setProperty ("nome", usuario.getNome ());
        node.setProperty ("idade", usuario.getIdade ());
        node.setProperty ("cpf", usuario.getCpf ());
        transaction.success ();
        graph.shutdown ();
            if(node != null) {
               return true;
            }
            return false;
    }

    public boolean deletarUsuario(String cpf){
        Node node = getNode ();
        node = (Node) buscarUsuario (cpf);
        node.delete ();
        transaction.success ();
        graph.shutdown ();
        if(node != null) {
            return true;
        }
            return false;
    }

    public boolean criarPublicacao(Usuario user, String texto){
        Node node = getNode ();
        node = (Node) buscarUsuario (user.getCpf ());
        Node node2 = getNode ();
        node2.setProperty ("publicacao", texto);

        node.createRelationshipTo (node2, Relacionamentos.COMENTARIO);

        transaction.success ();
        graph.shutdown ();
        return true;
    }

    public void seguirUsuario(Usuario usuario1, Usuario usuario2){
        Node node = (Node) buscarUsuario (usuario1.getCpf ());
        Node node2 = (Node) buscarUsuario (usuario2.getCpf ());

        node.createRelationshipTo (node2, Relacionamentos.SEGUIR);

        transaction.success ();
        graph.shutdown ();
    }

    public List listarAmigosDosAmigos(Usuario user) {
        Node node = (Node) buscarUsuario (user.getCpf ());

        Iterable<Relationship> relacionamentos = node.getRelationships (Relacionamentos.SEGUIR, Direction.OUTGOING);
        List amigos = new ArrayList ();
        for(Relationship rel: relacionamentos){
            Node node2 = (Node) buscarUsuario ((String) rel.getEndNode ().getProperty ("cpf"));
            Iterable<Relationship> relacionamentos2 = node.getRelationships (Relacionamentos.SEGUIR, Direction.OUTGOING);
            for(Relationship rel2: relacionamentos2){
               amigos.add (rel2.getEndNode ().getProperty ("nome"));
            }

            }
            return amigos;
    }
}
